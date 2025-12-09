package com.aeserp.spring_boot_erp.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.aeserp.spring_boot_erp.dao.UserRepository;
import com.aeserp.spring_boot_erp.entity.User;
import com.aeserp.spring_boot_erp.projection.UserListProjection;
import com.aeserp.spring_boot_erp.service.UserService; 
import com.aeserp.spring_boot_erp.specifications.UserSpecifications;

import jakarta.validation.Valid; 

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService; 

    @Autowired
    private ProjectionFactory projectionFactory;

    // --- 1. ENDPOINT DE RECHERCHE & FILTRAGE ---

    @GetMapping("/search/filter")
    public Page<UserListProjection> filterUsers(
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) Boolean isActivated,
            @RequestParam(required = false) Long note, // Note est un Long
            @RequestParam(required = false) String keyword, 
            Pageable pageable
    ) {
    	
    	// GESTION DU FILTRE GLOBAL
        if (keyword != null && !keyword.isEmpty()) {
            // Note: findByKeyword dans UserRepository doit retourner Page<UserListProjection>
            return userRepository.findByKeyword(keyword, pageable); 
        }
        
        // GESTION DES FILTRES DE COLONNES
        Specification<User> spec = UserSpecifications.withFilters(
            matricule, firstName, position, grade, isActivated, note
        );
        
        Page<User> userPage = userRepository.findAll(spec, pageable);
        
        // Mapper le contenu à la projection (correct)
        return userPage.map(user -> 
            projectionFactory.createProjection(UserListProjection.class, user)
        );
    }
    
    // --- 2. ENDPOINT DE CRÉATION (POST) ---
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        // Le service gère l'encodage et la validation unique
        return userService.createUser(user);
    }
    
    // --- 3. ENDPOINT DE MISE À JOUR (PUT) ---
    @PutMapping("/{matricule}")
    public User updateUser(@PathVariable Long matricule, @Valid @RequestBody User userDetails) {
        // Le service gère la mise à jour conditionnelle du mot de passe et l'unicité
        return userService.updateUser(matricule, userDetails);
    }

    // --- 4. ENDPOINT DE SUPPRESSION (DELETE) ---
    @DeleteMapping("/{matricule}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long matricule) {
        userService.deleteUser(matricule);
    }
    
    /**
     * Récupère la liste des positions uniques existantes dans la base de données.
     * Endpoint: GET /api/users/lookup/positions
     */
    @GetMapping("/lookup/positions")
    public List<String> getUniquePositions() {
        return userRepository.findDistinctPositions();
    }

    /**
     * Récupère la liste des grades uniques existantes dans la base de données.
     * Endpoint: GET /api/users/lookup/grades
     */
    @GetMapping("/lookup/grades")
    public List<String> getUniqueGrades() {
        return userRepository.findDistinctGrades();
    }
    
    @GetMapping("/exists")
    public Map<String, Boolean> checkUserExists(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email) {

        boolean usernameExists = username != null && !username.isEmpty() && userRepository.existsByUsername(username);
        boolean emailExists = email != null && !email.isEmpty() && userRepository.existsByEmail(email);

        Map<String, Boolean> response = new HashMap<>();
        response.put("usernameExists", usernameExists);
        response.put("emailExists", emailExists);
        return response;
    }
}