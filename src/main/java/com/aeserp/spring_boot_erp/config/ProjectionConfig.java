package com.aeserp.spring_boot_erp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory; // Importez cette implémentation

@Configuration
public class ProjectionConfig {

    /**
     * Expose le SpelAwareProxyProjectionFactory de Spring Data, qui est 
     * l'implémentation concrète nécessaire pour les projections dans les contrôleurs.
     * Ceci est injecté dans UserController.
     */
    @Bean
    public ProjectionFactory projectionFactory() {
        // La ProjectionFactory par défaut est l'implémentation SpEL-aware.
        return new SpelAwareProxyProjectionFactory(); 
    }
}