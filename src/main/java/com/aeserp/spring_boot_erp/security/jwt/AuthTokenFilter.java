package com.aeserp.spring_boot_erp.security.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aeserp.spring_boot_erp.service.UserDetailsServiceImpl;

/**
 * Filtre personnalisé pour intercepter les requêtes et valider le JWT.
 */
public class AuthTokenFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestUri = request.getRequestURI(); 
        // Correction 1: Utilisation de la concaténation
        logger.info("Processing request for URI: " + requestUri); 

        try {
            String jwt = parseJwt(request); 
            
            // Correction 2: Utilisation de la concaténation
            logger.info("JWT status for " + requestUri + ": " + (jwt != null ? "FOUND" : "NOT FOUND"));
            
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) { 
                // Correction 3: Utilisation de la concaténation
                logger.info("JWT validated successfully for URI: " + requestUri); 
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username); 
                
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                
            } else if (jwt != null) {
                // Correction 4: Utilisation de la concaténation
                logger.warn("JWT validation failed for URI: " + requestUri);
            }
            
        } catch (Exception e) {
            // Correction 5: La méthode error accepte le message + l'objet Exception (e)
            logger.error("!!! FATAL EXCEPTION defining user authentication for URI: " + requestUri + " - Message: " + e.getMessage(), e); 
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrait le JWT de l'en-tête Authorization (Bearer Token).
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}