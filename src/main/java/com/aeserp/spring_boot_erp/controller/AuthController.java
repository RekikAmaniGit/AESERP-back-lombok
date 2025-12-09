package com.aeserp.spring_boot_erp.controller;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aeserp.spring_boot_erp.payload.request.LoginRequest;
import com.aeserp.spring_boot_erp.payload.response.AuthResponse;
import com.aeserp.spring_boot_erp.security.UserDetailsImpl;
import com.aeserp.spring_boot_erp.security.jwt.JwtUtils; // 🔑 NOUVEL IMPORT

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired 
    AuthenticationManager authenticationManager; 

    @Autowired // 🔑 Injecter le JwtUtils
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 🔑 Générer le JWT
        String jwt = jwtUtils.generateJwtToken(authentication); 
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        
        // 🔑 Retourner le token dans la réponse
        AuthResponse response = new AuthResponse(
                userDetails.getMatricule(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
        
        response.setToken(jwt); // Le token est ajouté au DTO de réponse
        
        return ResponseEntity.ok(response);
    }
}