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
    public ResponseEntity<User> createProfile(@RequestBody User user){
        logger.debug("Received user: " + user);
        User savedUser = userService.saveUser(user);
        logger.debug("Saved user: " + savedUser);
        return ResponseEntity
                .status(HttpStatus.SC_CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedUser);
    }

    // Get a User by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        logger.debug("Searching for user with id : " + id);
        Optional<User> user = userService.getUserById(id);
        logger.debug("Found User : " + user);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get all Users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.debug("Acessing all users");
        List<User> users = userService.getAllUsers();
        logger.debug("All user: " + users);
        return new ResponseEntity<>(users, HttpStatusCode.valueOf(HttpStatus.SC_OK));
    }

    // Delete a User
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        logger.debug("Deleting user with id : " + id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/connect")
    public void connectToProfile(){

    }

    @PutMapping("/password")
    public void changePassword(){

    }

}
