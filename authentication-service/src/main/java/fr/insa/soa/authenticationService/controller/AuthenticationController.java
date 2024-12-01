package fr.insa.soa.authenticationService.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.insa.soa.authenticationService.model.User;
import fr.insa.soa.authenticationService.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;

    // Endpoint de connexion
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String userName, @RequestParam String password) {
        boolean isAuthenticated = authService.login(userName, password);
        if (isAuthenticated) {
            return ResponseEntity.ok("Utilisateur connecté !");
        }
        return ResponseEntity.status(401).body("Nom d'utilisateur ou mot de passe incorrect.");
    }

    // Endpoint pour récupérer l'utilisateur connecté
    @GetMapping("/connected")
    public ResponseEntity<User> getConnectedUser() {
        User connectedUser = authService.getConnectedUser();
        if (connectedUser != null) {
            return ResponseEntity.ok(connectedUser);
        }
        return ResponseEntity.status(401).body(null);  // Aucun utilisateur connecté
    }

    // Endpoint pour se déconnecter
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        authService.logout();
        return ResponseEntity.ok("Utilisateur déconnecté !");
    }
}
