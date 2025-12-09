package com.aeserp.spring_boot_erp.specifications;

import com.aeserp.spring_boot_erp.entity.User;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UserSpecifications {

    // 🔑 CORRECTION CLÉ : Le type générique est Specification<User>.
    // J'ai aussi changé le type du paramètre 'note' en Long.
    public static Specification<User> withFilters(String matricule, String firstName, String position, String grade, Boolean isActivated, Long note) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. FILTRE MATRICULE
            if (matricule != null && !matricule.isEmpty()) {
                try {
                    // 🔑 CORRECTION: Recherche matricule basé sur Long (égal à)
                    Long matriculeId = Long.valueOf(matricule);
                    predicates.add(cb.equal(root.get("matricule"), matriculeId));
                } catch (NumberFormatException e) {
                    // Ne rien faire si le matricule n'est pas un nombre valide
                }
            }
            
            // 2. FILTRE PRENOM
            if (firstName != null && !firstName.isEmpty()) {
                // Si le frontend envoie plusieurs prénoms séparés par des virgules (MultiSelect)
                String[] firstNames = firstName.split(",");
                List<Predicate> firstNamePredicates = new ArrayList<>();
                for (String name : firstNames) {
                    firstNamePredicates.add(cb.equal(cb.lower(root.get("firstName")), name.toLowerCase()));
                }
                predicates.add(cb.or(firstNamePredicates.toArray(new Predicate[0])));
            }
            
            // 3. FILTRE POSITION
            if (position != null && !position.isEmpty()) {
                // 🔑 CORRECTION: Recherche exacte pour la position (Equals)
                predicates.add(cb.equal(root.get("position"), position));
            }
            
            // 4. FILTRE GRADE
            if (grade != null && !grade.isEmpty()) {
                predicates.add(cb.equal(root.get("grade"), grade));
            }
            
            // 5. FILTRE ACTIVATION
            if (isActivated != null) {
                predicates.add(cb.equal(root.get("isActivated"), isActivated));
            }
            
            // 6. FILTRE NOTE (Supérieure ou égale)
            if (note != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("note"), note));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}