package com.aeserp.spring_boot_erp.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Imports de validation et de sérialisation
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Imports JPA
import jakarta.persistence.*; 
import lombok.*;

@Entity
@Table(name="users") 
@Getter // Utilisation de @Getter et @Setter au lieu de @Data
@Setter // pour exclure explicitement le mot de passe
@NoArgsConstructor // Ajout des constructeurs requis par JPA
@AllArgsConstructor
@ToString(exclude = {"password", "managedEmployees", "manager", "roles"}) // Exclusion des relations ET du mot de passe
@EqualsAndHashCode(exclude = {"managedEmployees", "roles"}) // Exclusion des collections pour le HashCode
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matricule;

    @Column(unique = true, nullable = false, length = 20) 
    @NotBlank(message = "Le nom d'utilisateur ne peut être vide.")
    @Size(max = 20)
    private String username;
    
    private String photoUrl; 

    @Column(unique = true, nullable = false, length = 50) 
    @NotBlank(message = "L'email ne peut être vide.")
    @Size(max = 50)
    @Email(message = "Format d'email invalide.")
    private String email;
    
    @Column(nullable = false, length = 120) 
    @NotBlank(message = "Le mot de passe ne peut être vide.")
    @Size(max = 120)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // <--NE JAMAIS SÉRIALISER LE MOT DE PASSE EN JSON
    private String password;
    
    @Column(length = 50)
    @NotBlank(message = "Le prénom ne peut être vide.")
    @Size(max = 50)
    private String firstName;
    
    @Column(length = 50)
    @NotBlank(message = "Le nom de famille ne peut être vide.")
    @Size(max = 50)
    private String lastName;

    @Column(length = 50)
    @NotBlank
    @Size(max = 50)
    private String companyName;

    @Column(length = 50)
    @NotBlank
    @Size(max = 50)
    private String phoneNumber;

    @Column(length = 50)
    @NotBlank
    @Size(max = 50)
    private String country;

    @Column(length = 50)
    @NotBlank
    @Size(max = 50)
    private String position;
    
    @Column(nullable = false) 
    private LocalDate hireDate = LocalDate.now();
    
    @Column(length = 50)
    @NotBlank
    @Size(max = 50)
    private String grade;

    // --- Relations JPA ---

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "manager_matricule")
    @JsonIgnoreProperties("managedEmployees") 
    private User manager;

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY, cascade = CascadeType.ALL) 
    @JsonIgnoreProperties("manager") 
    private List<User> managedEmployees;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", 
               joinColumns = @JoinColumn(name = "user_matricule"), 
               inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    
    public void addRole(Role role) {
        this.roles.add(role);
    }
}