package fr.insa.soa.orchestratorService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import fr.insa.soa.orchestratorService.model.Mission;
@RestController
@RequestMapping("/orchestrator")
public class OrchestratorController {

    @Autowired
    private RestTemplate restTemplate;

    // Récupérer une mission par ID
    @GetMapping("/mission/{id}")
    public Mission getMissionById(@PathVariable int id) {
        String missionServiceUrl = "http://MissionMicroservice/mission/" + id;
        return restTemplate.getForObject(missionServiceUrl, Mission.class);
    }
    
    @GetMapping()
    public int test() {
        return 2;
    }
    
    
    @GetMapping("/missions")
    public List<Object> getAllMissions() {
        // URL du service des missions (passant par Eureka)
        String missionServiceUrl = "http://MissionMicroservice/mission";

        // Appeler l'endpoint du service des missions
        List<Object> missions = restTemplate.getForObject(missionServiceUrl, List.class);

        return missions;
    }

 
}
