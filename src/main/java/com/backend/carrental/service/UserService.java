package com.backend.carrental.service;

import com.backend.carrental.dao.AdminDao;
import com.backend.carrental.dao.UserDao;
import com.backend.carrental.domain.Role;
import com.backend.carrental.domain.User;
import com.backend.carrental.domain.enumeration.UserRole;
import com.backend.carrental.exception.AuthException;
import com.backend.carrental.exception.BadRequestException;
import com.backend.carrental.exception.ConflictException;
import com.backend.carrental.exception.ResourceNotFoundException;
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

    private final PasswordEncoder passwordEncoder;

    private final static String USER_NOT_FOUND_MSG = "user with id %d not found";

    private final static String ID_NOT_FOUND_MSG = "user with id %s not found";

    public List<User> fetchAllUsers(){
        return userRepository.findAll();
    }

    public User findByIdAdmin(Long id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));
    }

    public UserDao findById(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ID_NOT_FOUND_MSG, id)));

        return new UserDao(user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getEmail(),
                user.getAddress(), user.getZipCode(), user.getRoles());
    }

    public void register(User user) throws BadRequestException {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("Error: Email is already in use!");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

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

    public void updateUser(Long id, UserDao userDao) throws BadRequestException {

        boolean emailExists = userRepository.existsByEmail(userDao.getEmail());
        Optional<User> userDetails = userRepository.findById(id);

        if (emailExists && !userDao.getEmail().equals(userDetails.get().getEmail())){
            throw new ConflictException("Error: Email is already in use!");
        }

        userRepository.update(id, userDao.getFirstName(), userDao.getLastName(), userDao.getPhoneNumber(),
                userDao.getEmail(), userDao.getAddress(), userDao.getZipCode());
    }

    public void updateUserAuth(Long id, AdminDao adminDao) throws BadRequestException {

        boolean emailExists = userRepository.existsByEmail(adminDao.getEmail());
        Optional<User> userDetails = userRepository.findById(id);

        if (emailExists && !adminDao.getEmail().equals(userDetails.get().getEmail())){
            throw new ConflictException("Error: Email is already in use!");
        }

        String encodedPassword = passwordEncoder.encode(adminDao.getPassword());

        adminDao.setPassword(encodedPassword);

        Set<String> userRoles = adminDao.getRole();
        Set<Role> roles = addRoles(userRoles);

        User user = new User(id, adminDao.getFirstName(), adminDao.getLastName(), adminDao.getPassword(),
                adminDao.getPhoneNumber(), adminDao.getEmail(), adminDao.getAddress(), adminDao.getZipCode(), roles);

        userRepository.save(user);
    }

    public void updatePassword(Long id, String newPassword, String oldPassword) throws BadRequestException {
        Optional<User> user = userRepository.findById(id);
        if (!(BCrypt.hashpw(oldPassword, user.get().getPassword()).equals(user.get().getPassword())))
            throw new BadRequestException("password does not match");

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.get().setPassword(hashedPassword);
        userRepository.save(user.get());
    }

    public void removeById(Long id) throws ResourceNotFoundException {
        boolean userExists = userRepository.existsById(id);

        if (!userExists){
            throw new ResourceNotFoundException("user does not exist");
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
