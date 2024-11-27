package fr.insa.soa.UserMangementMS.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.insa.soa.UserMangementMS.DataObjectAccess.UserDAO;
import fr.insa.soa.UserMangementMS.Model.User;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    private UserDAO userDAO = new UserDAO();

    // Afficher tous les utilisateurs
    @GetMapping
    public List<User> getAllUsers() throws Exception {
        try {
            return userDAO.getAllUsers();
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des utilisateurs : {}", e.getMessage(), e);
            throw new Exception("Erreur lors de la récupération des utilisateurs.");
        }
    }

    // Créer un nouvel utilisateur
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getRole() <= 0) {
            return ResponseEntity
                    .badRequest()
                    .body("Les champs nécessaires doivent être fournis : username, password et role.");
        }

        try {
            User createdUser = userDAO.createUser(user);
            return ResponseEntity.status(201).body(createdUser);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'utilisateur : {}", e.getMessage(), e);
            return ResponseEntity
                    .status(500)
                    .body("Erreur lors de la création de l'utilisateur : " + e.getMessage());
        }
    }

    // Mettre à jour un utilisateur
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable int userId, @RequestBody User updatedUser) {
        try {
            User existingUser = userDAO.getUserById(userId); // Retourne directement un User ou null

            if (existingUser != null) { // Vérifie si l'utilisateur existe
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setPassword(updatedUser.getPassword());
                existingUser.setRole(updatedUser.getRole());

                boolean isUpdated = userDAO.updateUser(existingUser); // Appelle la méthode de mise à jour
                if (isUpdated) {
                    return ResponseEntity.ok("Utilisateur mis à jour avec succès.");
                } else {
                    return ResponseEntity.status(500).body("Échec de la mise à jour de l'utilisateur.");
                }
            } else {
                return ResponseEntity.status(404).body("Utilisateur introuvable.");
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de l'utilisateur : {}", e.getMessage(), e);
            return ResponseEntity
                    .status(500)
                    .body("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }

    // Supprimer un utilisateur
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable int userId) {
        try {
            boolean deleted = userDAO.deleteUser(userId);
            if (deleted) {
                return ResponseEntity.ok("Utilisateur supprimé avec succès.");
            } else {
                return ResponseEntity.status(404).body("Utilisateur introuvable.");
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'utilisateur : {}", e.getMessage(), e);
            return ResponseEntity
                    .status(500)
                    .body("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }
}
