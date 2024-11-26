package fr.insa.soa.userservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private int id;
    private String roleName;
    private String userName;
    private String password;
}