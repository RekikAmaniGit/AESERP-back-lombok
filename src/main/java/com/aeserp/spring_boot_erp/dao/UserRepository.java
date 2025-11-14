package com.aeserp.spring_boot_erp.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.aeserp.spring_boot_erp.entity.User;
import com.aeserp.spring_boot_erp.projection.UserListProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@CrossOrigin("http://localhost:4200")

@RepositoryRestResource(excerptProjection = UserListProjection.class)
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	
	// 1. Recherche par nom d'utilisateur
    Optional<User> findByUsername(String username);

    // 2. Recherche par email
    Optional<User> findByEmail(String email);

    // 3. Vérification de l'existence par nom d'utilisateur
    boolean existsByUsername(String username);

    // 4. Vérification de l'existence par email
    boolean existsByEmail(String email);
    
    
    
// ------------------------------------
// --- AJOUTS ET MISE À JOUR FILTRAGE ---
// ------------------------------------
    

    
 // Recherche globale (inchangée)
 @Query("SELECT u FROM User u WHERE " +
        "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
        "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
        "LOWER(CAST(u.matricule AS text)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
        "LOWER(u.position) LIKE LOWER(CONCAT('%', :keyword, '%'))")
 Page<User> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
}