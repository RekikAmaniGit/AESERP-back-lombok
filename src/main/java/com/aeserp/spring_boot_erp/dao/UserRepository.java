package com.aeserp.spring_boot_erp.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aeserp.spring_boot_erp.entity.User;
import com.aeserp.spring_boot_erp.projection.UserMinimalProjection;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>{
	// 1. Recherche par nom d'utilisateur (ESSENTIEL pour l'authentification Spring Security)
    Optional<User> findByUsername(String username);

    // 2. Recherche par email (souvent utilisé pour la connexion ou la réinitialisation de mot de passe)
    Optional<User> findByEmail(String email);

    // 3. Vérification de l'existence par nom d'utilisateur (pour l'enregistrement)
    boolean existsByUsername(String username);

    // 4. Vérification de l'existence par email (pour l'enregistrement)
    boolean existsByEmail(String email);
    
    @Query("SELECT u.matricule as matricule, u.firstName as firstName, u.lastName as lastName, u.photoUrl as photoUrl FROM User u")
    List<UserMinimalProjection> findAllMinimalProjection();
}
