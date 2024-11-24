package fr.insa.soa.RestProject;

import fr.insa.soa.RestProject.Database.DatabaseConnection;

import java.sql.Connection;

public class testDatabaseConnection {

public static void main(String[] args) {
    try {
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            System.out.println("Connexion réussie !");
        } else {
            System.out.println("Échec de la connexion.");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}