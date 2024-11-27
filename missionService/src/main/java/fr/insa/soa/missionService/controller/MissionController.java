package fr.insa.soa.missionService.controller;

import java.util.List;
import java.util.Optional;

import fr.insa.soa.missionService.service.MissionService;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.insa.soa.missionService.model.Mission;

@RestController
@RequestMapping("/missions")
public class MissionController {

    private static final Logger logger = LoggerFactory.getLogger(MissionController.class);

    @Autowired
    private MissionService missionService;

    // Get all missions
    @GetMapping
    public ResponseEntity<List<Mission>> getAllMissions() {
        List<Mission> missions = missionService.getAllMissions();
        return ResponseEntity.ok(missions);
    }

    // Get on mission by id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getMissionById(@PathVariable int id) {
        Optional<Mission> mission = missionService.getMissionById(id);

        if (!(mission.isPresent())) {
            logger.warn("User with id " + id + " not found");
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body("User with ID " + id + " not found");
        }

        logger.debug("Found User : " + mission.get());
        return ResponseEntity.ok(mission.get());
    }

    // Get missions with a certain status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Mission>> getMissionsByStatus(@PathVariable int status) {
        List<Mission> missions = missionService.getMissionsByStatus(status);

        if (missions.isEmpty()) {
            return ResponseEntity.noContent().build(); // No missions with the given status
        }
        return ResponseEntity.ok(missions);
    }

    // Create new mission
    @PostMapping("/create")
    public ResponseEntity<?> createMission(@RequestBody Mission mission) {
        if (mission.getPersonInNeedId() < 0 || mission.getName() == null || mission.getDescription() == null) {
            logger.warn("Invalid mission request : missing fields");
            return ResponseEntity.badRequest()
                    .body("Invalid mission request : missing fields");
        }
        Mission createdMission = missionService.saveMission(mission);
        return ResponseEntity.ok(createdMission);
    }

    // Issue a decision over a mission
    @PutMapping("/{missionId}/status")
    public ResponseEntity<?> acceptOrRefuseMission(
            @PathVariable int missionId,
            @RequestParam("validationStatus") boolean isAccepted,
            @RequestParam(value = "Description", required = false) String description) {
        int status = isAccepted ? 2 : 3; // 2: Accepted, 3: Refused

        Optional<Mission> updatedMission = missionService.updateMissionStatus(missionId, status, description);
        if (updatedMission.isPresent()) {
            return ResponseEntity.ok(updatedMission.get());
        } else {
            return ResponseEntity.status(404).body("Mission not found");
        }
    }

    // Issue a decision over a mission
    @PutMapping("/{missionId}/completed")
    public ResponseEntity<?> completedMission(@PathVariable int missionId) {

        Optional<Mission> updatedMission = missionService.updateMissionStatus(missionId, 1, "");
        if (updatedMission.isPresent()) {
            return ResponseEntity.ok(updatedMission.get());
        } else {
            return ResponseEntity.status(404).body("Mission not found");
        }
    }

    // Delete a mission
    @DeleteMapping("/{missionId}")
    public ResponseEntity<?> deleteMission(@PathVariable int missionId) {
        if (missionService.getMissionById(missionId).isPresent()) {
            missionService.deleteMission(missionId);
            return ResponseEntity.ok("Mission sucessfully deleted");
        } else {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body("User with ID " + missionId + " not found");
        }
    }
}
