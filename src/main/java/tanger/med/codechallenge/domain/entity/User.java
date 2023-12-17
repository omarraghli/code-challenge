package tanger.med.codechallenge.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tanger.med.codechallenge.domain.enums.Role;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Entity class representing a user in the application.
 */
@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "app_user")
public class User implements UserDetails {

    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * The birth date of the user.
     */
    private Date birthDate;

    /**
     * The city where the user is located.
     */
    private String city;

    /**
     * The country where the user is located.
     */
    private String country;

    /**
     * The avatar of the user.
     */
    private String avatar;

    /**
     * The company where the user works.
     */
    private String company;

    /**
     * The job position of the user.
     */
    private String jobPosition;

    /**
     * The mobile number of the user.
     */
    private String mobile;

    /**
     * The unique username of the user.
     */
    @Column(unique = true)
    private String username;

    /**
     * The unique email address of the user.
     */
    @Column(unique = true)
    private String email;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The role of the user (e.g., ADMIN, USER).
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * The list of tokens associated with the user.
     */
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername(){
        return username;
    }
}
