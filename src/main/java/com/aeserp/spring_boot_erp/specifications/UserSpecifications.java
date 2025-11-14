package com.aeserp.spring_boot_erp.specifications;

import com.aeserp.spring_boot_erp.entity.User;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UserSpecifications {

    public static Specification<User> withFilters(String matricule, String firstName, String position, String grade, Boolean isActivated, Long note) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (matricule != null && !matricule.isEmpty()) {
                predicates.add(cb.like(root.get("matricule").as(String.class), "%" + matricule + "%"));
            }
            if (firstName != null && !firstName.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%"));
            }
            if (position != null && !position.isEmpty()) {
                predicates.add(cb.equal(root.get("position"), position));
            }
            if (grade != null && !grade.isEmpty()) {
                predicates.add(cb.equal(root.get("grade"), grade));
            }
            if (isActivated != null) {
                predicates.add(cb.equal(root.get("isActivated"), isActivated));
            }
            if (note != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("note"), note));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}