package fr.insa.soa.userservice.controller;

import fr.insa.soa.userservice.model.User;
import fr.insa.soa.userservice.service.UserService;
import jakarta.transaction.Transactional;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @PostMapping("/create")
    public ResponseEntity<Object> createProfile(@RequestBody User user) {
        logger.debug("Received user: " + user);
        // TODO : Handling edge cases could be added to front-end, before calling controller

        // If no userName specified
        if (user.getUserName() == null || user.getUserName().isEmpty()) {
            logger.warn("Invalid user data: Missing username");
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST)
                    .body("Invalid user data: Missing username");
        }

        // If no password specified
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            logger.warn("Invalid user data: Missing password");
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST)
                    .body("Invalid user data: Missing password");
        }

        User savedUser = userService.saveUser(user);
        logger.debug("Saved user: " + savedUser);

        return ResponseEntity.status(HttpStatus.SC_CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedUser);
    }

    // Get a User by ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable int id) {
        logger.debug("Searching for user with id : " + id);

        Optional<User> user = userService.getUserById(id);
        if (!(user.isPresent())) {
            logger.warn("User with id " + id + " not found");
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body("User with ID " + id + " not found");
        }

        logger.debug("Found User : " + user.get());
        return ResponseEntity.ok(user.get());
    }
    
    @GetMapping("/connect")
    public ResponseEntity<User> getConnectedUser() {
        try {
            // Simule un utilisateur connecté (exemple d'un utilisateur avec ID = 1)
            User user = new User(1, "personInNeed","connectedUser", "password123");

            // Retourner une ResponseEntity avec un statut HTTP 200 (OK)
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // Retourner une ResponseEntity avec un statut HTTP 500 en cas d'erreur
            return ResponseEntity.status(500).body(null);
        }
    }

    // Get all Users
    @GetMapping("/all")
    public ResponseEntity<Object> getAllUsers() {
        logger.debug("Accessing all users");
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            logger.info("No users found in the system");
            return ResponseEntity.status(HttpStatus.SC_OK)
                    .body("No users found in the system");
        }

        logger.debug("All users: " + users);
        return ResponseEntity.ok(users);
    }

    // Delete a User
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> deleteUser(@PathVariable int id) {
        logger.debug("Deleting user with id : " + id);

        Optional<User> user = userService.getUserById(id);
        if (!(user.isPresent())) {
            logger.warn("User with id " + id + " not found for deletion");
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body("User with ID " + id + " not found");
        }

        userService.deleteUser(id);
        logger.debug("User with id " + id + " deleted successfully");
        return ResponseEntity.noContent().build();
    }

  
    @PutMapping("/password")
    public ResponseEntity<Object> changePassword(@RequestParam int userId, @RequestParam String newPassword) {
        logger.debug("Request to change password for user ID: " + userId);

        if (newPassword == null || newPassword.isEmpty()) {
            logger.warn("Password change request missing new password");
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST)
                    .body("New password cannot be empty");
        }

        Optional<User> user = userService.getUserById(userId);
        if (!(user.isPresent())) {
            logger.warn("User with ID " + userId + " not found for password change");
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body("User with ID " + userId + " not found");
        }

        user.get().setPassword(newPassword);
        userService.saveUser(user.get());
        logger.debug("Password updated successfully for user ID: " + userId);

        return ResponseEntity.ok(user.get());
    }


}





