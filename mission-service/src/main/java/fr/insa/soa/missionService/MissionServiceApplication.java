package fr.insa.soa.missionService;

import com.netflix.discovery.EurekaNamespace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;

@SpringBootApplication

public class MissionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MissionServiceApplication.class, args);
	}

}
