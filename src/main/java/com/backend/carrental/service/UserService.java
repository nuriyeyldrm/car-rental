package com.backend.carrental.service;

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
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public void register(User user) throws BadRequestException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ConflictException("Error: Username is already taken!");
        }

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

    public void login(String username, String password) throws AuthException {
        try {
            Optional<User> user = userRepository.findByUsername(username);

            if (!BCrypt.checkpw(password, user.get().getPassword()))
                throw new AuthException("invalid credentials");
        } catch (Exception e) {
            throw new AuthException("invalid credentials");
        }
    }
}
