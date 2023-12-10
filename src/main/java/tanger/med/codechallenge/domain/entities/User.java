package tanger.med.codechallenge.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import tanger.med.codechallenge.domain.enums.Role;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "app_user") // Change the table name to avoid conflicts with reserved keywords with H2 Database
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String city;
    private String country;
    private String avatar;
    private String company;
    private String jobPosition;
    private String mobile;
    private String username;
    private String email;
    private String password;
    //Enum
    private Role role;
}
