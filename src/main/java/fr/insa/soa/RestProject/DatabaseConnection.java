package fr.insa.soa.RestProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConnection {
    private static Connection connection;

    // Méthode pour établir la connexion (appelée automatiquement au chargement de la classe)
    static {
        try {
            initializeConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection initialization failed: " + e.getMessage());
        }
    }

    // Méthode pour initialiser la connexion
    private static void initializeConnection() throws Exception {
        if (connection == null || connection.isClosed()) {
            Properties properties = new Properties();
            InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("dbconfig.properties");
            if (input == null) {
                throw new RuntimeException("Unable to find dbconfig.properties");
            }
            properties.load(input);

            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            
            // Charger explicitement le driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Établir la connexion
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connexion réussie à la base de données !");
        } else {
            System.out.println("Connexion existante réutilisée.");
        }
    }

    // Méthode pour obtenir la connexion existante
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                initializeConnection(); // Réinitialiser la connexion si elle a été fermée
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while getting the database connection: " + e.getMessage());
        }
        return connection;
    }

    // Méthode pour fermer explicitement la connexion
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion à la base de données fermée !");
            } else {
                System.out.println("La connexion est déjà fermée ou inexistante.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while closing the database connection: " + e.getMessage());
        } finally {
            connection = null; // Assurez-vous que l'objet de connexion est nul après fermeture
        }
    }
}
