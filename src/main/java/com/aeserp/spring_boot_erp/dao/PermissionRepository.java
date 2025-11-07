package com.aeserp.spring_boot_erp.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aeserp.spring_boot_erp.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>{
	// 1. Recherche par nom (utilité pour les contrôles d'accès)
    Optional<Permission> findByName(String name);
}
