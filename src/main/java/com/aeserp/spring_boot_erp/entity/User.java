package com.aeserp.spring_boot_erp.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Imports de validation et de sérialisation
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull; // 🔑 Ajout
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Imports JPA
import jakarta.persistence.*; 
import lombok.*;

@Entity
@Table(name="users") 
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
@ToString(exclude = {"password", "managedEmployees", "manager", "roles"}) 
@EqualsAndHashCode(exclude = {"managedEmployees", "roles"}) 
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matricule;

    @Column(unique = true, nullable = false, length = 20) 
    @NotBlank(message = "Le nom d'utilisateur ne peut être vide.")
    @Size(max = 20)
    private String username; // 🔑 CORRECTION: Rendu private

    private String photoUrl; 

    @Column(unique = true, nullable = false, length = 50) 
    @NotBlank(message = "L'email ne peut être vide.")
    @Size(max = 50)
    @Email(message = "Format d'email invalide.")
    private String email;
    
    @Column(nullable = false, length = 120) 
    // 🔑 IMPORTANT: Le mot de passe ne peut pas être @NotBlank pour la mise à jour si on ne le change pas. La validation est déplacée dans le UserService/Controller.
    @Size(max = 120)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) 
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
    // 🔑 CORRECTION: Ajout de valeurs par défaut pour les champs non nullables
    private String companyName = "N/A";

    @Column(length = 50)
    private String phoneNumber = "N/A";

    @Column(length = 50)
    private String country = "N/A";

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

    @Column(nullable = false)
    @NotNull // 🔑 Ajout de @NotNull car c'est un Boolean (nullable=false)
    private Boolean isActivated = false; 
    
    @Column(length = 50)
    @NotNull // 🔑 Ajout de @NotNull
    private Long note = 0L; // 🔑 Initialisation par défaut

 // --- Relations JPA (inchangées, mais les optimisations sont conservées) ---

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "manager_matricule")
    @JsonIgnoreProperties({"managedEmployees", "handler", "hibernateLazyInitializer"}) 
    private User manager;

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY, cascade = CascadeType.ALL) 
    @JsonIgnoreProperties({"manager", "handler", "hibernateLazyInitializer"}) 
    private List<User> managedEmployees;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"}) 
    @JoinTable(name = "user_role", 
               joinColumns = @JoinColumn(name = "user_matricule"), 
               inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    
    public void addRole(Role role) {
        this.roles.add(role);
    }
}