package fr.insa.soa.userservice.model;

import jakarta.persistence.*;

@Entity
public class Validator extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /*
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user; // Linking to the base User class
    */
}
