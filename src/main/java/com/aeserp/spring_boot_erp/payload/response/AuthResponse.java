package com.aeserp.spring_boot_erp.payload.response;

import java.util.List;
import lombok.Data;

@Data
public class AuthResponse {
    private Long matricule;
    private String username;
    private String email;
    private List<String> roles;
    private String token; // Utilisé si vous passez à JWT

    public AuthResponse(Long matricule, String username, String email, List<String> roles) {
        this.matricule = matricule;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}