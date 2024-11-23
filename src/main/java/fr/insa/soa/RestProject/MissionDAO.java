package fr.insa.soa.RestProject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MissionDAO {

    // Ajouter une mission
    public Mission createMission(Mission mission) throws SQLException {
        String sql = "INSERT INTO missions (name, description, person_in_need_id, statut) VALUES (?, ?, ?, ?)";
        
        // Utiliser la connexion persistante
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, mission.getName());
            statement.setString(2, mission.getDescription());
            statement.setInt(3, mission.getPersonInNeedId());
            statement.setInt(4, 0); // 0 : en attente

            int rows = statement.executeUpdate();
            if (rows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    mission.setMissionId(generatedKeys.getInt(1));
                }
            }
            return mission;
        }
    }

    // Récupérer toutes les missions
    public List<Mission> getAllMissions() throws SQLException {
        List<Mission> missions = new ArrayList<>();
        String sql = "SELECT * FROM missions";
        
        // Utiliser la connexion persistante
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Mission mission = new Mission();
                mission.setMissionId(resultSet.getInt("mission_id"));
                mission.setName(resultSet.getString("name"));
                mission.setDescription(resultSet.getString("description"));
                mission.setPersonInNeedId(resultSet.getInt("person_in_need_id"));
                mission.setStatut(resultSet.getInt("statut"));
                mission.setHelperId(resultSet.getInt("helper_id"));
                missions.add(mission);
            }
        }
        return missions;
    }

    // Accepter ou refuser une mission
    public boolean updateMissionStatus(int missionId, int statut) throws SQLException {
        String sql = "UPDATE missions SET statut = ? WHERE mission_id = ?";
        
        // Utiliser la connexion persistante
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, statut);
          
            statement.setInt(2, missionId);

            int rows = statement.executeUpdate();
            return rows > 0;
        }
    }

    // Supprimer une mission
    public boolean deleteMission(int missionId) throws SQLException {
        String sql = "DELETE FROM missions WHERE mission_id = ?";
        
        // Utiliser la connexion persistante
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, missionId);
            int rows = statement.executeUpdate();
            return rows > 0;
        }
    }
}
