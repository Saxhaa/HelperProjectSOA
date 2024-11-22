package fr.insa.soa.RestProject;

import java.sql.Connection;

public class test {

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