package fr.insa.soa.orchestratorService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import fr.insa.soa.orchestratorService.model.Mission;
import fr.insa.soa.orchestratorService.model.User;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orchestrator")
public class OrchestratorController {

    private static final String MISSION_SERVICE_URL = "http://MISSION-SERVICE/missions";
    private static final String USER_SERVICE_URL = "http://USER-SERVICE/user";
    private static final String AUTH_SERVICE_URL = "http://AUTHENTICATION-SERVICE/auth";

    @Autowired
    private RestTemplate restTemplate;

    // Test simple pour vérifier que l'orchestrateur fonctionne
    @GetMapping("/test")
    public String test() {
        return "Orchestrator is up and running!";
    }

    // Créer une mission
    @PostMapping("/mission/create")
    public ResponseEntity<?> createMission(@RequestParam String name, @RequestParam String description) {
        try {
            // Créer une nouvelle mission avec les données des paramètres
            Mission newMission = new Mission();
            newMission.setName(name);
            newMission.setDescription(description);

            // Récupérer l'utilisateur connecté
            User connectedUser = getConnectedUser(); // Utilisation de la méthode pour récupérer l'utilisateur connecté

            // Mettre à jour l'ID de la personne dans le besoin avec l'utilisateur connecté
            newMission.setPersonInNeedId(connectedUser.getId());
            newMission.setHelperId(0);  // Aucun volontaire par défaut
            newMission.setStatus(0);    // Statut initial : en attente

            // Envoi de la requête au microservice Mission
            String missionUrl = MISSION_SERVICE_URL + "/create";
            Mission createdMission = restTemplate.postForObject(missionUrl, newMission, Mission.class);

            // Retourner la réponse
            return ResponseEntity.ok(createdMission);
        } catch (HttpClientErrorException.BadRequest e) {
            return ResponseEntity.status(400).body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la création de la mission : " + e.getMessage());
        }
    }

    // Récupérer une mission par ID
    @GetMapping("/missions/{id}")
    public ResponseEntity<?> getMissionById(@PathVariable int id) {
        try {
            String url = MISSION_SERVICE_URL + "/" + id;
            Mission mission = restTemplate.getForObject(url, Mission.class);
            return ResponseEntity.ok(mission);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la récupération de la mission : " + e.getMessage());
        }
    }

    // Récupérer toutes les missions
    @GetMapping("/missions")
    public ResponseEntity<List<Mission>> getAllMissions() {
        try {
            List<Mission> missions = restTemplate.getForObject(MISSION_SERVICE_URL, List.class);
            return ResponseEntity.ok(missions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Récupérer toutes les missions associées à un statut donné
    @GetMapping("/missions/status/{status}")
    public ResponseEntity<?> getMissionsByStatus(@PathVariable int status) {
        try {
            String url = MISSION_SERVICE_URL + "/status/" + status;
            List<Mission> missions = restTemplate.getForObject(url, List.class);
            return ResponseEntity.ok(missions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la récupération des missions par statut : " + e.getMessage());
        }
    }

    @PutMapping("/missions/{id}/status")
    public ResponseEntity<?> modifyMission(@PathVariable int id, @RequestParam boolean validationStatus) {
        try {
            // Récupérer l'utilisateur connecté
            User connectedUser = getConnectedUser();
            if (connectedUser == null) {
                return ResponseEntity.status(401).body("Utilisateur non connecté");
            }

            // Build the URL to the acceptOrRefuseMission endpoint
            String url = MISSION_SERVICE_URL + "/{id}/status?validationStatus={validationStatus}";

            // Create request parameters
            Map<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(id));
            params.put("validationStatus", String.valueOf(validationStatus));

            // Send PUT request using RestTemplate
            ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.PUT, null, Object.class, params);

            // Return the response from the microservice (Mission service)
            return response;

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la modification de la mission : " + e.getMessage());
        }
    }



    // Supprimer une mission
    @DeleteMapping("/missions/{id}/delete")
    public ResponseEntity<?> deleteMission(@PathVariable int id) {
        try {
            String missionUrl = MISSION_SERVICE_URL + "/" + id;
            restTemplate.delete(missionUrl);
            return ResponseEntity.ok("Mission supprimée avec succès !");
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("Mission not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la suppression de la mission : " + e.getMessage());
        }
    }

    // Récupérer un utilisateur par ID
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        try {
            String url = USER_SERVICE_URL + "/" + id;
            User user = restTemplate.getForObject(url, User.class);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
    }

    // Récupérer tous les utilisateurs
    @GetMapping("/user/all")
    public ResponseEntity<?> getAllUsers() {
        try {
            String url = USER_SERVICE_URL + "/all";
            List<User> users = restTemplate.getForObject(url, List.class);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
    }

    // Créer un nouvel utilisateur
    @PostMapping("/user/create")
    public ResponseEntity<?> createUser(@RequestBody User newUser) {
        try {
            // Envoi de la requête pour créer l'utilisateur dans le microservice User
            String userUrl = USER_SERVICE_URL + "/create";
            User createdUser = restTemplate.postForObject(userUrl, newUser, User.class);

            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la création de l'utilisateur : " + e.getMessage());
        }
    }

    // Mettre à jour un utilisateur
    @PutMapping("/user/{id}/{password}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @PathVariable String password) {
        try {
            // Construct the URL to call the changePassword endpoint using RestTemplate
            String url = UriComponentsBuilder
                    .fromHttpUrl(USER_SERVICE_URL + "/{id}/{password}")  // The URL for changing the password
                    .buildAndExpand(id, password)  // Inject 'id' and 'password' into the URL
                    .toUriString();

            // Send PUT request using RestTemplate to change the password
            ResponseEntity<Object> response = restTemplate.exchange(
                    url, HttpMethod.PUT, null, Object.class
            );

            // Check if the password change was successful
            if (response.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok("User updated successfully, password changed.");
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating the user: " + e.getMessage());
        }
    }



    // Supprimer un utilisateur
    @DeleteMapping("/user/{id}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        try {
            String userUrl = USER_SERVICE_URL + "/" + id;
            restTemplate.delete(userUrl);
            return ResponseEntity.ok("Utilisateur supprimé avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }

    // Récupérer l'utilisateur "connecté"
    @GetMapping("/connected")
    public User getConnectedUser() {
        try {
            String url = AUTH_SERVICE_URL + "/connected";
            return restTemplate.getForObject(url, User.class);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur connecté : " + e.getMessage());
        }
    }
    
 // Connexion de l'utilisateur
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String userName, @RequestParam String password) {
        try {
            // Appel au service Authentication pour connecter l'utilisateur
            String loginUrl = UriComponentsBuilder.fromHttpUrl(AUTH_SERVICE_URL + "/login")
                    .queryParam("userName", userName)
                    .queryParam("password", password)
                    .toUriString();

            String response = restTemplate.postForObject(loginUrl, null, String.class);

            if ("Utilisateur connecté !".equals(response)) {
                return ResponseEntity.ok("Utilisateur connecté !");
            } else {
                return ResponseEntity.status(401).body("Nom d'utilisateur ou mot de passe incorrect.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la connexion : " + e.getMessage());
        }
    }

    // Déconnexion de l'utilisateur
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        try {
            // Appel au service Authentication pour déconnecter l'utilisateur
            String logoutUrl = AUTH_SERVICE_URL + "/logout";
            restTemplate.postForObject(logoutUrl, null, String.class);
            return ResponseEntity.ok("Utilisateur déconnecté !");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la déconnexion : " + e.getMessage());
        }
    }

}

