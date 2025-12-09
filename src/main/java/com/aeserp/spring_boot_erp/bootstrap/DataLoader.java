package com.aeserp.spring_boot_erp.bootstrap;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.aeserp.spring_boot_erp.dao.RoleRepository;
import com.aeserp.spring_boot_erp.dao.UserRepository;
import com.aeserp.spring_boot_erp.entity.Role;
import com.aeserp.spring_boot_erp.entity.User;

@Configuration
public class DataLoader {

    // 💡 Définition des Rôles utilisés
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_MANAGER = "ROLE_MANAGER";
    public static final String ROLE_USER = "ROLE_USER";

    @Bean
    @Transactional // S'assurer que les opérations de base de données se font dans une transaction
    public CommandLineRunner initData(RoleRepository roleRepository, 
                                      UserRepository userRepository, 
                                      PasswordEncoder passwordEncoder) {
        
        return args -> {
            
            // --- 1. Création et Sauvegarde des Rôles ---
            
            // Vérifie et crée le rôle ROLE_ADMIN
            Optional<Role> adminRoleOpt = roleRepository.findByName(ROLE_ADMIN);
            Role adminRole = adminRoleOpt.orElseGet(() -> {
                Role newRole = new Role(ROLE_ADMIN);
                return roleRepository.save(newRole);
            });
            
            // Vérifie et crée le rôle ROLE_MANAGER
            Optional<Role> managerRoleOpt = roleRepository.findByName(ROLE_MANAGER);
            Role managerRole = managerRoleOpt.orElseGet(() -> {
                Role newRole = new Role(ROLE_MANAGER);
                return roleRepository.save(newRole);
            });
            
            // Vérifie et crée le rôle ROLE_USER
            Optional<Role> userRoleOpt = roleRepository.findByName(ROLE_USER);
            userRoleOpt.orElseGet(() -> {
                Role newRole = new Role(ROLE_USER);
                return roleRepository.save(newRole);
            });


            // --- 2. Création de l'Utilisateur de Test (Admin) ---
            
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@aeserp.com");
                // 🔑 Mot de passe encodé (vous utiliserez 'password' pour vous connecter)
                admin.setPassword(passwordEncoder.encode("password")); 
                admin.setFirstName("Super");
                admin.setLastName("Admin");
                admin.setPosition("MANAGER");
                admin.setGrade("Executive");
                admin.setIsActivated(true);
                admin.setNote(100L);
                admin.setHireDate(LocalDate.now());
                
                // Assigne le rôle ADMIN et MANAGER au compte de test
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                roles.add(managerRole);
                admin.setRoles(roles);
                
                userRepository.save(admin);
                System.out.println("✅ Utilisateur 'admin' créé avec succès. Mot de passe: password");
            }
            
            // --- 3. Création d'un Utilisateur Simple ---
            
            if (!userRepository.existsByUsername("test.user")) {
                User simpleUser = new User();
                simpleUser.setUsername("test.user");
                simpleUser.setEmail("user@aeserp.com");
                simpleUser.setPassword(passwordEncoder.encode("password"));
                simpleUser.setFirstName("John");
                simpleUser.setLastName("Doe");
                simpleUser.setPosition("Clerk");
                simpleUser.setGrade("Junior");
                simpleUser.setIsActivated(true);
                simpleUser.setNote(50L);
                simpleUser.setHireDate(LocalDate.now());
                
                // Assigne le rôle USER
                Role userRole = roleRepository.findByName(ROLE_USER).get();
                Set<Role> roles = new HashSet<>();
                roles.add(userRole);
                simpleUser.setRoles(roles);
                
                userRepository.save(simpleUser);
                System.out.println("✅ Utilisateur 'test.user' créé avec succès. Mot de passe: password");
            }
        };
    }
}