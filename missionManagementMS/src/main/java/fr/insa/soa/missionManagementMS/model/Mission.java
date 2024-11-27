package fr.insa.soa.missionManagementMS.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class Mission {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Génération automatique de l'ID
    private int missionId;

    private String name;
    private String description;

    private int personInNeedId; // ID du demandeur
    private int helperId; // ID du volontaire (par défaut 0)
    private int status; // 0: en attente, 1: en cours, 2: acceptée, 3: refusée

    public Mission() {
    }

    // Getters et Setters
    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPersonInNeedId() {
        return personInNeedId;
    }

    public void setPersonInNeedId(int personInNeedId) {
        this.personInNeedId = personInNeedId;
    }

    public int getHelperId() {
        return helperId;
    }

    public void setHelperId(int helperId) {
        this.helperId = helperId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
