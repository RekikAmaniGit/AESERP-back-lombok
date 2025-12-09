package com.aeserp.spring_boot_erp.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    
    @NotBlank
    private String username;

    @NotBlank
    private String password;

}