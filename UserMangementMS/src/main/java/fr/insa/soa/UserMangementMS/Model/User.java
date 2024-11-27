package fr.insa.soa.UserMangementMS.Model;

public class User {

    private int id; // Identifiant unique de l'utilisateur
    private String username; // Nom d'utilisateur
    private String password; // Mot de passe
    private int role; // Rôle de l'utilisateur (ex : 1 = Admin, 2 = Utilisateur standard, etc.)

    // Constructeur par défaut
    public User() {
    }

    // Constructeur avec paramètres
    public User(int id, String username, String password, int role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    // Méthode toString pour le debug et le logging
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}
