package com.aeserp.spring_boot_erp.security.jwt;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Gère l'erreur 401 (Unauthorized) lorsqu'un utilisateur non authentifié tente d'accéder à une ressource protégée.
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        
        logger.error("Erreur d'authentification non autorisée: {}", authException.getMessage());
        
        // Renvoie une erreur 401 avec un message clair
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Erreur: Non autorisé. Veuillez fournir un jeton JWT valide.");
    }
}