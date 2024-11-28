package fr.insa.soa.userservice;

import fr.insa.soa.userservice.model.User;
import fr.insa.soa.userservice.repository.UserRepository;
import fr.insa.soa.userservice.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    //private final Logger logger = org.apache.log4j.Logger.getLogger(UserServiceTests.class);
    private User user;

    @BeforeEach
    void setUp() {
        //logger.debug("Setting up...");
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);
        user.setUserName("John Doe");
        user.setPassword("password123");
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        //logger.debug("Starting test for saving user...");
        User savedUser = userService.saveUser(user);
        //logger.debug("User saved with name: {}", savedUser.getUserName());

        assertNotNull(savedUser);
        assertEquals("John Doe", savedUser.getUserName());
        verify(userRepository, times(1)).save(user);
        //logger.debug("Finished test for saving user...");
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1)).thenReturn(user);

        //logger.debug("Starting test for getting user by id...");
        Optional<User> foundUser = userService.getUserById(1);
        //logger.debug("User found with name: {}", foundUser.get().getUserName());

        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getUserName());
        //logger.debug("Finished test for getting user by id...");
    }
}
