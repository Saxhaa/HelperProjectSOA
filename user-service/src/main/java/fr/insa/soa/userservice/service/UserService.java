package fr.insa.soa.userservice.service;

import fr.insa.soa.userservice.mapper.UserMapper;
import fr.insa.soa.userservice.model.User;
import fr.insa.soa.userservice.model.UserDTO;
import fr.insa.soa.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    //  private final UserMapper userMapper = UserMapper.INSTANCE;

    // Create or Update a User
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Get a User by ID
    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(userRepository.findById(id));
    }

    // Get all Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete User by ID
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
