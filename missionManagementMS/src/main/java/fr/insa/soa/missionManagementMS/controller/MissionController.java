package fr.insa.soa.missionManagementMS.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.insa.soa.missionManagementMS.model.Mission;
import fr.insa.soa.missionManagementMS.repository.MissionRepository;

@RestController
@RequestMapping("/missions")
public class MissionController {

    private static final Logger logger = LoggerFactory.getLogger(MissionController.class);

    @Autowired
    private MissionRepository missionRepository;

    // Obtenir toutes les missions
    @GetMapping
    public ResponseEntity<List<Mission>> getAllMissions() {
        List<Mission> missions = missionRepository.findAll();
        return ResponseEntity.ok(missions);
    }

    // Créer une nouvelle mission
    @PostMapping("/create")
    public ResponseEntity<?> postDemandOrProposition(@RequestBody Mission mission) {
        if (mission.getPersonInNeedId() <= 0 || mission.getName() == null || mission.getDescription() == null) {
            return ResponseEntity.badRequest()
                    .body("Tous les champs nécessaires doivent être fournis : ID du demandeur, nom, description.");
        }

        try {
            Mission createdMission = missionRepository.save(mission);
            return ResponseEntity.status(201).body(createdMission);
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout de la mission : {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Erreur lors de l'ajout de la mission : " + e.getMessage());
        }
    }

    // Accepter ou refuser une mission
    @PutMapping("/{missionId}/decision")
    public ResponseEntity<?> acceptOrRefuseMission(
            @PathVariable int missionId,
            @RequestParam("accepté") boolean accept,
            @RequestParam(value = "motif", required = false) String motif) {
        Optional<Mission> optionalMission = missionRepository.findById(missionId);
        if (optionalMission.isPresent()) {
            Mission mission = optionalMission.get();
            mission.setStatus(accept ? 2 : 3); // 2 : Acceptée, 3 : Refusée
            if (!accept) {
                mission.setDescription(motif);
            }
            missionRepository.save(mission);
            return ResponseEntity.ok(accept ? "Mission acceptée." : "Mission refusée. Motif : " + motif);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer une mission
    @DeleteMapping("/{missionId}")
    public ResponseEntity<?> supprimerMission(@PathVariable int missionId) {
        if (missionRepository.existsById(missionId)) {
            missionRepository.deleteById(missionId);
            return ResponseEntity.ok("Mission supprimée avec succès.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
