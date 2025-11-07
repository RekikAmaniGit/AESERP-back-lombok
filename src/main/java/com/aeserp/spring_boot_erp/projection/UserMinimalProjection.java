package com.aeserp.spring_boot_erp.projection;

public interface UserMinimalProjection {
    Long getMatricule(); // Utilisé comme valeur d'identification
    String getFirstName();
    String getLastName();
    String getPhotoUrl();
}