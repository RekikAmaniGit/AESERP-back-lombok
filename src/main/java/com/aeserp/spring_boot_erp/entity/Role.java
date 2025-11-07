package com.aeserp.spring_boot_erp.entity;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name="roles")
@Data
@EqualsAndHashCode(exclude = {"permissions"}) // Exclure les collections pour éviter les problèmes de performance/récursivité
@ToString(exclude = {"permissions"}) // Exclure la collection de ToString
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, unique = true, nullable = false)
    private String name;

    // Utilisation de FetchType.LAZY est généralement préférable pour les ManyToMany
    @ManyToMany(fetch = FetchType.LAZY) 
    @JoinTable(
        name = "role_permission",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    
    private Set<Permission> permissions = new HashSet<>(); 

    
    public Role() {}

    public Role(String name) {
        this.name = name;
    }
}