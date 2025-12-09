package com.aeserp.spring_boot_erp.config;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; 
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; 
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.aeserp.spring_boot_erp.security.jwt.AuthEntryPointJwt; 
import com.aeserp.spring_boot_erp.security.jwt.AuthTokenFilter;
import com.aeserp.spring_boot_erp.service.UserDetailsServiceImpl;
import com.aeserp.spring_boot_erp.bootstrap.DataLoader; 

import java.util.Arrays;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	// 🔑 Injecter les composants JWT
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    // 🔑 Bean pour le filtre JWT
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    
    // 💡 Bean PasswordEncoder : Gardé ici pour la simplicité
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Désactiver CSRF 
            .csrf(csrf -> csrf.disable()) 
            
            // 2. Configurer CORS 
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) 
            
            // 3. Gestion des Exceptions: Utiliser notre gestionnaire 401
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)) 
            
            // 4. Gestion de Session: Rendre l'API sans état (stateless)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
         // 5. Définir les règles d'autorisation
            .authorizeHttpRequests(auth -> auth
                // 🔑 AJOUT CLÉ: Autoriser TOUS les OPTIONS (nécessaire pour CORS pre-flight)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // <-- AJOUTER CETTE LIGNE
                
                // Autoriser l'accès non authentifié aux endpoints d'AUTH
                .requestMatchers("/api/auth/**").permitAll() 
                
                // Exemple: Nécessite ROLE_ADMIN ou ROLE_MANAGER pour accéder aux données des utilisateurs
                .requestMatchers("/api/users/**").hasAnyAuthority(DataLoader.ROLE_ADMIN, DataLoader.ROLE_MANAGER)
                
                // Exiger l'authentification pour toutes les autres requêtes
                .anyRequest().authenticated()
            );
        
        // 6. Ajouter le filtre JWT avant le filtre de base de Spring Security
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuration CORS pour autoriser les requêtes depuis l'application Angular (localhost:4200).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // ⬅️ L'URL de votre frontend
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // Autorise l'envoi de cookies/Basic Auth/Headers
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); 
        return source;
    }
    
 // Ajoutez ceci pour garantir que votre service et encodeur sont utilisés
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // ⬅️ Votre service
        authProvider.setPasswordEncoder(passwordEncoder);       // ⬅️ Votre encodeur
        return authProvider;
    }
    
    /**
     * Expose le AuthenticationManager comme un Bean.
     * Le service UserDetailsServiceImpl sera automatiquement utilisé.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}