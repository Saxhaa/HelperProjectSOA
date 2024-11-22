package fr.insa.soa.RestProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConnection {
    private static Connection connection;

    static {
        try {
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

           
            connection = DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
