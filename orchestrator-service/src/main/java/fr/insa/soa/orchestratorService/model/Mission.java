package fr.insa.soa.orchestratorService.model;



import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "missions")
public class Mission {
	
	public Mission() {
	}

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Génération automatique de l'ID
    private int missionId;

    @Column(name = "mission_name")
    @JsonProperty("missionName")
    private String name;

    @Column(name = "description")
    @JsonProperty("description")
    private String description;

    @Column(name = "person_in_need_id")
    @JsonProperty("personInNeedId")
    private int personInNeedId; // ID du demandeur

    @Column(name = "helper_id")
    @JsonProperty("HelperId")
    private int helperId; // ID du volontaire (par défaut 0)

    @Column(name = "status")
    @JsonProperty("status")
    private int status; // 0: en attente, 1: en cours, 2: acceptée, 3: refusée

}
