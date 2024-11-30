package fr.insa.soa.orchestratorService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import fr.insa.soa.orchestratorService.model.Mission;
import fr.insa.soa.orchestratorService.model.User;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orchestrator")
public class OrchestratorController {

    private static final String MISSION_SERVICE_URL = "http://MissionMicroservice/missions";
    private static final String USER_SERVICE_URL = "http://UserMicroservice/user";
    private static final String AUTH_SERVICE_URL = "http://AuthenticationMicroservice/auth";

    @Autowired
    private RestTemplate restTemplate;

    // Test simple pour vérifier que l'orchestrateur fonctionne
    @GetMapping("/test")
    public String test() {
        return "Orchestrator is up and running!";
    }

    // Récupérer une mission par ID
    @GetMapping("/mission/{id}")
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
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            String url = USER_SERVICE_URL + "/all";
            List<User> users = restTemplate.getForObject(url, List.class);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
    }

    // Récupérer l'utilisateur "connecté"
    @GetMapping("/user/connected")
    public User getConnectedUser() {
        try {
            String url = AUTH_SERVICE_URL + "/connected";  // Assurez-vous que cet endpoint existe dans le microservice Auth
            return restTemplate.getForObject(url, User.class);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de l'utilisateur connecté : " + e.getMessage());
        }
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
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la création de la mission : " + e.getMessage());
        }
    }


 // Modifier une mission
    @PutMapping("/mission/{id}/modify")
    public ResponseEntity<?> modifyMission(@PathVariable int id, @RequestBody Mission updatedMission) {
        try {
            // Récupérer l'utilisateur connecté
            User connectedUser = getConnectedUser();

            // Préparer la requête pour modifier une mission
            updatedMission.setMissionId(id);
            updatedMission.setPersonInNeedId(connectedUser.getId());

            // Envoi de la requête pour modifier la mission dans le microservice Mission
            String missionUrl = MISSION_SERVICE_URL + "/" + id + "/update";
            restTemplate.put(missionUrl, updatedMission);

            return ResponseEntity.ok("Mission modifiée avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la modification de la mission : " + e.getMessage());
        }
    }


    // Supprimer une mission
    @DeleteMapping("/mission/{id}/delete")
    public ResponseEntity<?> deleteMission(@PathVariable int id) {
        try {
            String missionUrl = MISSION_SERVICE_URL + "/" + id;
            restTemplate.delete(missionUrl);
            return ResponseEntity.ok("Mission supprimée avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la suppression de la mission : " + e.getMessage());
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
    @PutMapping("/user/{id}/update")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        try {
            updatedUser.setId(id);

            // Envoi de la requête pour mettre à jour l'utilisateur dans le microservice User
            String userUrl = USER_SERVICE_URL + "/" + id + "/update";
            restTemplate.put(userUrl, updatedUser);

            return ResponseEntity.ok("Utilisateur mis à jour avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
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
    
 // Connexion de l'utilisateur
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String userName, @RequestParam String password) {
        try {
            // Appel au service Authentication pour connecter l'utilisateur
            String loginUrl = "http://AuthenticationMicroservice/auth/login?userName=" + userName + "&password=" + password;
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
            String logoutUrl = "http://AuthenticationMicroservice/auth/logout";
            restTemplate.postForObject(logoutUrl, null, String.class);
            return ResponseEntity.ok("Utilisateur déconnecté !");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la déconnexion : " + e.getMessage());
        }
    }

}

