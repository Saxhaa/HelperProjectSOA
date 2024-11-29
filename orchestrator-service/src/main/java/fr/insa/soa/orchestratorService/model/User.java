package fr.insa.soa.orchestratorService.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
	
	public User() {
	}
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "role_name")
    @JsonProperty("roleName")
    private String roleName;

    @Column(name = "user_name")
    @JsonProperty("userName")
    private String userName;

    @Column(name = "password")
    private String password;

    @Override
    public String toString() {
        return "User{id=" + id + ", userName='" + userName + "', password='" + password + "'}";
    }

}
