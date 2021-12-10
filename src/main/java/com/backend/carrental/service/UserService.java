package com.backend.carrental.service;

import com.backend.carrental.dto.AdminDTO;
import com.backend.carrental.dto.UserDTO;
import com.backend.carrental.domain.Role;
import com.backend.carrental.domain.User;
import com.backend.carrental.domain.enumeration.UserRole;
import com.backend.carrental.exception.AuthException;
import com.backend.carrental.exception.BadRequestException;
import com.backend.carrental.exception.ConflictException;
import com.backend.carrental.exception.ResourceNotFoundException;
import com.backend.carrental.projection.ProjectUser;
import com.backend.carrental.repository.ReservationRepository;
import com.backend.carrental.repository.RoleRepository;
import com.backend.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final ReservationRepository reservationRepository;

    private final PasswordEncoder passwordEncoder;

    private final static String USER_NOT_FOUND_MSG = "user with id %d not found";

    public List<ProjectUser> fetchAllUsers(){
        return userRepository.findAllBy();
    }

    public UserDTO findById(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));

        UserDTO userDTO = new UserDTO();
        userDTO.setRoles(user.getRole());

        return new UserDTO(user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getEmail(),
                user.getAddress(), user.getZipCode(), userDTO.getRoles(), user.getBuiltIn());
    }

    public void register(User user) throws BadRequestException {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("Error: Email is already in use!");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        user.setBuiltIn(false);

        Set<Role> roles = new HashSet<>();
        Role customerRole = roleRepository.findByName(UserRole.ROLE_CUSTOMER)
                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
        roles.add(customerRole);

        user.setRoles(roles);
        userRepository.save(user);
    }

    public void login(String email, String password) throws AuthException {
        try {
            Optional<User> user = userRepository.findByEmail(email);

            if (!BCrypt.checkpw(password, user.get().getPassword()))
                throw new AuthException("invalid credentials");
        } catch (Exception e) {
            throw new AuthException("invalid credentials");
        }
    }

    public void addUserAuth(AdminDTO adminDTO) throws BadRequestException {

        boolean emailExists = userRepository.existsByEmail(adminDTO.getEmail());

        if (emailExists){
            throw new ConflictException("Error: Email is already in use!");
        }

        String encodedPassword = passwordEncoder.encode(adminDTO.getPassword());
        adminDTO.setPassword(encodedPassword);
        adminDTO.setBuiltIn(false);

        Set<String> userRoles = adminDTO.getRoles();
        Set<Role> roles = addRoles(userRoles);

        User user = new User(adminDTO.getFirstName(), adminDTO.getLastName(), adminDTO.getPassword(),
                adminDTO.getPhoneNumber(), adminDTO.getEmail(), adminDTO.getAddress(), adminDTO.getZipCode(),
                roles, adminDTO.getBuiltIn());

        userRepository.save(user);
    }

    public void updateUser(Long id, UserDTO userDao) throws BadRequestException {

        boolean emailExists = userRepository.existsByEmail(userDao.getEmail());
        Optional<User> userDetails = userRepository.findById(id);

        if (userDetails.get().getBuiltIn()){
            throw new ResourceNotFoundException("You dont have permission to update user info!");
        }

        if (emailExists && !userDao.getEmail().equals(userDetails.get().getEmail())){
            throw new ConflictException("Error: Email is already in use!");
        }

        userRepository.update(id, userDao.getFirstName(), userDao.getLastName(), userDao.getPhoneNumber(),
                userDao.getEmail(), userDao.getAddress(), userDao.getZipCode());
    }

    public void updateUserAuth(Long id, AdminDTO adminDTO) throws BadRequestException {

        boolean emailExists = userRepository.existsByEmail(adminDTO.getEmail());
        Optional<User> userDetails = userRepository.findById(id);

        if (userDetails.get().getBuiltIn()){
            throw new ResourceNotFoundException("You dont have permission to update user info!");
        }

        adminDTO.setBuiltIn(false);

        if (emailExists && !adminDTO.getEmail().equals(userDetails.get().getEmail())){
            throw new ConflictException("Error: Email is already in use!");
        }

        if (adminDTO.getPassword() == null) {
            adminDTO.setPassword(userDetails.get().getPassword());
        }

        else {
            String encodedPassword = passwordEncoder.encode(adminDTO.getPassword());
            adminDTO.setPassword(encodedPassword);
        }

        Set<String> userRoles = adminDTO.getRoles();
        Set<Role> roles = addRoles(userRoles);

        User user = new User(id, adminDTO.getFirstName(), adminDTO.getLastName(), adminDTO.getPassword(),
                adminDTO.getPhoneNumber(), adminDTO.getEmail(), adminDTO.getAddress(), adminDTO.getZipCode(),
                roles, adminDTO.getBuiltIn());

        userRepository.save(user);
    }

    public void updatePassword(Long id, String newPassword, String oldPassword) throws BadRequestException {
        Optional<User> user = userRepository.findById(id);

        if (user.get().getBuiltIn()){
            throw new ResourceNotFoundException("You dont have permission to update password!");
        }

        if (!(BCrypt.hashpw(oldPassword, user.get().getPassword()).equals(user.get().getPassword())))
            throw new BadRequestException("password does not match");

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.get().setPassword(hashedPassword);
        userRepository.save(user.get());
    }

    public void removeById(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));
        boolean reservation = reservationRepository.existsByUserId(user);

        if (reservation)
            throw new ResourceNotFoundException("Reservation(s) exist for user!");

        if (user.getBuiltIn()){
            throw new ResourceNotFoundException("You dont have permission to delete user!");
        }

        userRepository.deleteById(id);
    }

    public Set<Role> addRoles(Set<String> userRoles) {
        Set<Role> roles = new HashSet<>();

        if (userRoles == null) {
            Role userRole = roleRepository.findByName(UserRole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            userRoles.forEach(role -> {
                switch (role) {
                    case "Administrator":
                        Role adminRole = roleRepository.findByName(UserRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "CustomerService":
                        Role customerServiceRole = roleRepository.findByName(UserRole.ROLE_CUSTOMER_SERVICE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(customerServiceRole);

                        break;

                    case "Manager":
                        Role managerRole = roleRepository.findByName(UserRole.ROLE_MANAGER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(managerRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(UserRole.ROLE_CUSTOMER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        return roles;
    }
}
