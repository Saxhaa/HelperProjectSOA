package fr.insa.soa.UserMangementMS.DataObjectAccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import fr.insa.soa.UserMangementMS.Database.DatabaseConnection;
import fr.insa.soa.UserMangementMS.Model.User;

public class UserDAO {

    /** Crée un utilisateur dans la base de données, avec une connexion persistante
     *
     * @param user L'objet utilisateur à créer
     * @return L'objet utilisateur créé avec l'ID généré
     */
    public User createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getRole());

            int rows = statement.executeUpdate();
            if (rows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
            return user;
        }
    }

    /** Récupère tous les utilisateurs de la base de données
     *
     * @return Une liste de tous les utilisateurs
     */
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getInt("role"));
                users.add(user);
            }
        }
        return users;
    }

    /** Met à jour un utilisateur dans la base de données
     *
     * @param user L'objet utilisateur avec les nouvelles valeurs
     * @return Un boolean indiquant si la mise à jour a été effectuée
     */
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getRole());
            statement.setInt(4, user.getId());

            int rows = statement.executeUpdate();
            return rows > 0;
        }
    }

    /** Supprime un utilisateur de la base de données
     *
     * @param userId L'ID de l'utilisateur à supprimer
     * @return Un boolean indiquant si la suppression a été effectuée
     */
    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            int rows = statement.executeUpdate();
            return rows > 0;
        }
    }

    /** Récupère un utilisateur par son ID
     *
     * @param userId L'ID de l'utilisateur à rechercher
     * @return Un objet User si trouvé, sinon null
     */
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRole(resultSet.getInt("role"));
                    return user;
                }
            }
        }
        return null;
    }
}
