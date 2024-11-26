package fr.insa.soa.userservice.model;

import fr.insa.soa.userservice.model.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
public class Helper extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;

    /*
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user; // Linking to the base User class

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
     */

}
