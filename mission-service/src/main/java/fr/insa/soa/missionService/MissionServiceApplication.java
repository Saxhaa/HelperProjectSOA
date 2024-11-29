package fr.insa.soa.missionService;

import com.netflix.discovery.EurekaNamespace;
import fr.insa.soa.missionService.config.DataSourceConfig;
import fr.insa.soa.missionService.config.DatabaseProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(DatabaseProperties.class)
public class MissionServiceApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(MissionServiceApplication.class, args);
		SpringApplication.run(MissionServiceApplication.class, args);
	}

}
