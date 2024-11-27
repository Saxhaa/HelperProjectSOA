package fr.insa.soa.userservice;

import fr.insa.soa.userservice.controller.UserController;
import fr.insa.soa.userservice.model.User;
import fr.insa.soa.userservice.repository.UserRepository;
import fr.insa.soa.userservice.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @Autowired
    private UserRepository userRepository; // This will interact with the real DB

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setUserName("John Doe");
        user.setPassword("password123");
    }

    @Test
    void testCreateUser() throws Exception {
        //when(userService.saveUser(user)).thenReturn(user);

        mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"John Doe\", \"password\":\"password123\"}"))
                .andDo(print())  // Add this to print the request and response details to the console
                .andExpect(status().isCreated());
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getUserById(1)).thenReturn(Optional.ofNullable(user));

        mockMvc.perform(get("/user/1"))
                .andDo(print())  // Add this to print the request and response details to the console
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("John Doe"));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/user/1"))
                .andDo(print())  // Add this to print the request and response details to the console
                .andExpect(status().isNoContent());
    }
}
