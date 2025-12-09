package com.aeserp.spring_boot_erp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aeserp.spring_boot_erp.dao.UserRepository;
import com.aeserp.spring_boot_erp.entity.User;
import com.aeserp.spring_boot_erp.security.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    /**
     * Charge l'utilisateur à partir du nom d'utilisateur (username).
     * C'est la méthode clé utilisée par Spring Security pour l'authentification.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Chercher l'utilisateur dans la base de données
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec nom: " + username));

        // 2. Mapper l'entité User à l'implémentation de sécurité (UserDetailsImpl)
        return UserDetailsImpl.build(user);
    }
}