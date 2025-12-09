package com.aeserp.spring_boot_erp.service;

import com.aeserp.spring_boot_erp.dao.UserRepository;
import com.aeserp.spring_boot_erp.entity.User;
import com.aeserp.spring_boot_erp.exception.ResourceNotFoundException;
import com.aeserp.spring_boot_erp.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // 🔑 NOUVEAU: Import pour l'encodage du mot de passe
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired(required = false) // Le PasswordEncoder est généralement dans une configuration de sécurité
    private PasswordEncoder passwordEncoder;

    /**
     * Crée un nouvel utilisateur après validation des contraintes uniques
     * et encodage du mot de passe.
     */
    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Nom d'utilisateur déjà utilisé : " + user.getUsername());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email déjà utilisé : " + user.getEmail());
        }

        // 🔑 Encode le mot de passe avant la sauvegarde (ESSENTIEL !)
        if (passwordEncoder != null && user.getPassword() != null && !user.getPassword().isEmpty()) {
             user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire pour la création.");
        }
        
        // Initialisation des champs par défaut
        if (user.getMatricule() != null && user.getMatricule() > 0) user.setMatricule(null); // S'assurer de la création
        user.setHireDate(LocalDate.now());
        if (user.getIsActivated() == null) user.setIsActivated(false);
        if (user.getNote() == null) user.setNote(0L); 
        
        return userRepository.save(user);
    }

    /**
     * Met à jour un utilisateur existant.
     * Le matricule est l'identifiant de la ressource.
     */
    /**
     * Met à jour un utilisateur existant.
     */
    @Transactional
    public User updateUser(Long matricule, User userDetails) {
        User existingUser = userRepository.findById(matricule)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec le matricule: " + matricule));

        // Validation des contraintes uniques pour l'username et l'email (si changés)
        // ... (Ce code est correct, on le laisse) ...
        Optional<User> userByUsername = userRepository.findByUsername(userDetails.getUsername());
        if (userByUsername.isPresent() && !userByUsername.get().getMatricule().equals(matricule)) {
            throw new UserAlreadyExistsException("Nom d'utilisateur déjà utilisé.");
        }
        
        Optional<User> userByEmail = userRepository.findByEmail(userDetails.getEmail());
        if (userByEmail.isPresent() && !userByEmail.get().getMatricule().equals(matricule)) {
            throw new UserAlreadyExistsException("Email déjà utilisé.");
        }

        // Mise à jour des champs
        existingUser.setFirstName(userDetails.getFirstName());
        
        // ✅ CORRECTION CLÉ: Utiliser getLastName() de userDetails pour mettre à jour lastName.
        existingUser.setLastName(userDetails.getLastName()); 
        
        // Assurer que le username est mis à jour aussi (il est vérifié pour l'unicité)
        existingUser.setUsername(userDetails.getUsername()); 
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setCompanyName(userDetails.getCompanyName());
        existingUser.setPhoneNumber(userDetails.getPhoneNumber());
        existingUser.setCountry(userDetails.getCountry());
        existingUser.setPosition(userDetails.getPosition());
        existingUser.setGrade(userDetails.getGrade());
        existingUser.setIsActivated(userDetails.getIsActivated());
        existingUser.setNote(userDetails.getNote());
        
        // Mise à jour conditionnelle du mot de passe (ce bloc est correct)
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            if (passwordEncoder != null) {
                existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            } else {
                 throw new IllegalStateException("PasswordEncoder non configuré.");
            }
        }
        
        return userRepository.save(existingUser);
    }

    /**
     * Supprime un utilisateur par son matricule.
     */
    public void deleteUser(Long matricule) {
        if (!userRepository.existsById(matricule)) {
            throw new ResourceNotFoundException("Utilisateur non trouvé avec le matricule: " + matricule);
        }
        userRepository.deleteById(matricule);
    }
}