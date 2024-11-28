package fr.insa.soa.missionService;

import com.netflix.discovery.EurekaNamespace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication

public class MissionServiceApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(MissionServiceApplication.class, args);
		SpringApplication.run(MissionServiceApplication.class, args);
	}

}
