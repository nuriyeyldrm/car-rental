package com.backend.carrental.controller;

import com.backend.carrental.dao.AdminDao;
import com.backend.carrental.dao.UserDao;
import com.backend.carrental.domain.User;
import com.backend.carrental.security.jwt.JwtUtils;
import com.backend.carrental.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping()
public class UserController {

    public UserService userService;

    public AuthenticationManager authenticationManager;

    public JwtUtils jwtUtils;

    @GetMapping("/user/auth/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.fetchAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/user/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserByIdAdmin(@PathVariable Long id){
        User user = userService.findByIdAdmin(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<UserDao> getUserById(HttpServletRequest request){
        Long id = (Long) request.getAttribute("id");
        UserDao userDao = userService.findById(id);
        return new ResponseEntity<>(userDao, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Boolean>> registerUser(@Valid @RequestBody User user) {
        userService.register(user);

        Map<String, Boolean> map = new HashMap<>();
        map.put("User registered successfully!", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody Map<String, Object> userMap){
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");

        userService.login(email, password);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("/user")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> updateUser(HttpServletRequest request,
                                                           @Valid @RequestBody UserDao userDao) {
        Long id = (Long) request.getAttribute("id");
        userService.updateUser(id, userDao);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("/user/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> updateUserAuth(@PathVariable Long id,
                                                               @Valid @RequestBody AdminDao adminDao) {
        userService.updateUserAuth(id, adminDao);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PatchMapping("/user/auth")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> updatePassword(HttpServletRequest request,
                                                               @RequestBody Map<String, Object> userMap) {
        Long id = (Long) request.getAttribute("id");
        String newPassword = (String) userMap.get("newPassword");
        String oldPassword = (String) userMap.get("oldPassword");
        userService.updatePassword(id, newPassword, oldPassword);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id){
        userService.removeById(id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
