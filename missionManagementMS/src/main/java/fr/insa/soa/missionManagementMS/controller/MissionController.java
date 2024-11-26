package fr.insa.soa.missionManagementMS.controller;

import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.insa.soa.missionManagementMS.DataObjectAccess.MissionDAO;
import fr.insa.soa.missionManagementMS.model.Mission;


@RestController
public class MissionController {

	 private static final Logger logger = LoggerFactory.getLogger(MissionDAO.class);
	   private MissionDAO missionDAO = new MissionDAO();

	
    @GetMapping("/missions")
    public List<Mission> getAllMissions() throws Exception {
        try {
            return MissionDAO.getAllMissions();
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des missions : {}", e.getMessage(), e);
            throw new Exception("Erreur lors de la récupération des missions.");
        }
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> postDemandOrProposition(@RequestBody Mission mission) {
        if (mission.getPersonInNeedId() <= 0 || mission.getName() == null || mission.getDescription() == null) {
            return ResponseEntity
                    .status(HttpStatus.SC_BAD_REQUEST)
                    .body("Tous les champs nécessaires doivent être fournis : ID du demandeur, nom, description.");
        }

        try {
            Mission createdMission = missionDAO.createMission(mission);
            return ResponseEntity.status(HttpStatus.SC_CREATED).body(createdMission);
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout de la mission : {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.SC_SERVER_ERROR)
                    .body("Erreur lors de l'ajout de la mission : " + e.getMessage());
        }
    }
    
    // Accepter ou refuser une mission
    @PutMapping("/{missionId}/decision")
    public ResponseEntity<?> acceptOrRefuseMission(
            @PathVariable int missionId,
            @RequestParam("accepté") boolean accept,
            @RequestParam(value = "motif", required = false) String motif) {
        try {
            int status = accept ? 2 : 3; // 2 : Acceptée, 3 : Refusée
            String description = accept ? null : motif;

            boolean updated = missionDAO.updateMissionStatus(missionId, status);
            if (updated) {
                String responseMessage = accept ? "Mission acceptée." : "Mission refusée. Motif : " + motif;
                return ResponseEntity.ok(responseMessage);
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Mission introuvable.");
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la mission : {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.SC_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour de la mission : " + e.getMessage());
        }
    }

    // Supprimer une mission
    @DeleteMapping("/{missionId}")
    public ResponseEntity<?> supprimerMission(@PathVariable int missionId) {
        try {
            boolean deleted = missionDAO.deleteMission(missionId);
            if (deleted) {
                return ResponseEntity.ok("Mission supprimée avec succès.");
            } else {
                return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Mission introuvable.");
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la mission : {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.SC_SERVER_ERROR)
                    .body("Erreur lors de la suppression de la mission : " + e.getMessage());
        }
    }

    
}
