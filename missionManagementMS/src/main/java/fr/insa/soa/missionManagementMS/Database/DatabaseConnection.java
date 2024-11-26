package fr.insa.soa.missionManagementMS.Database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;



public class DatabaseConnection {
    private static Connection connection;

    // Méthode pour obtenir la connexion existante ou en créer une nouvelle
    public static synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                initializeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while getting the database connection: " + e.getMessage());
        }
        return connection;
    }

    // Méthode pour initialiser la connexion
    private static void initializeConnection() throws Exception {
        Properties properties = new Properties();
        InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("dbconfig.properties");
        if (input == null) {
            throw new RuntimeException("Unable to find dbconfig.properties");
        }
        properties.load(input);

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");


        Class.forName("com.mysql.cj.jdbc.Driver");

        // Établir la connexion
        connection = DriverManager.getConnection(url, username, password);
        System.out.println("Connexion réussie à la base de données !");
    }

    // Méthode pour fermer explicitement la connexion
    public static synchronized void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion à la base de données fermée !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while closing the database connection: " + e.getMessage());
        } finally {
            connection = null; // Garantir que l'objet de connexion est nul après fermeture
        }
    }
}
