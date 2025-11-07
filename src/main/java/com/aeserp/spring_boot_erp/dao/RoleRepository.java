package com.aeserp.spring_boot_erp.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aeserp.spring_boot_erp.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	// 1. Recherche par nom (cruciale pour l'authentification/sécurité)
    Optional<Role> findByName(String name);
}
