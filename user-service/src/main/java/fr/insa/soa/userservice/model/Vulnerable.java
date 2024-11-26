package fr.insa.soa.userservice.model;

import jakarta.persistence.*;

@Entity
public class Vulnerable extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private String city;

    /*
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user; // Linking to the base User class
     */
}
