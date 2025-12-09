package com.aeserp.spring_boot_erp.security;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.aeserp.spring_boot_erp.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Implémentation de UserDetails pour stocker les informations
 * de l'utilisateur authentifié.
 */
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long matricule;

    private String username;

    @JsonIgnore // Important: Ne pas exposer le mot de passe dans les réponses JSON
    private String password;

    private String email;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long matricule, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.matricule = matricule;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Construit un UserDetailsImpl à partir de l'entité User, mappant les Rôles
     * vers les GrantedAuthorities.
     */
    public static UserDetailsImpl build(User user) {
        // Mappe les rôles de l'utilisateur à des SimpleGrantedAuthority.
        // Spring Security utilise la convention "ROLE_" par défaut.
        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName())) 
            .collect(Collectors.toList());

        return new UserDetailsImpl(
            user.getMatricule(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            authorities);
    }

    // --- Getters spécifiques ---
    public Long getMatricule() {
        return matricule;
    }

    public String getEmail() {
        return email;
    }

    // --- Implémentation de UserDetails ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // Activer l'utilisateur par défaut (peut être lié à votre champ isActivated)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Optionnel : lier à votre champ isActivated
        // return user.getIsActivated(); 
        return true; 
    }

    // Pour garantir que les objets UserDetailsImpl sont comparés correctement
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(matricule, user.matricule);
    }
}