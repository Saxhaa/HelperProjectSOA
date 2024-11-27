package fr.insa.soa.missionService.service;

import fr.insa.soa.missionService.model.Mission;
import fr.insa.soa.missionService.repository.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MissionService {

    @Autowired
    private MissionRepository missionRepository;

    // Create or Update a Mission
    public Mission saveMission(Mission mission) {
        return missionRepository.save(mission);
    }

    // Get a Mission by ID
    public Optional<Mission> getMissionById(int id) {
        return missionRepository.findById(id);
    }

    // Get all missions with a specific status
    public List<Mission> getMissionsByStatus(int status) {
        return missionRepository.findByStatus(status);
    }

    // Get all Missions
    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    // Delete a Mission by ID
    public void deleteMission(int id) {
        missionRepository.deleteById(id);
    }

    // Update the status of a Mission
    public Optional<Mission> updateMissionStatus(int id, int status, String reason) {
        Optional<Mission> optionalMission = missionRepository.findById(id);
        if (optionalMission.isPresent()) {
            Mission mission = optionalMission.get();
            mission.setStatus(status);
            if (status == 3 && reason != null) { // 3: Refused
                mission.setDescription(reason);
                // TODO Update motif handling
                int v = 0;
            }
            missionRepository.save(mission);
            return Optional.of(mission);
        }
        return Optional.empty();
    }
}
