package com.aeserp.spring_boot_erp.projection;

import org.springframework.data.rest.core.config.Projection;

import com.aeserp.spring_boot_erp.entity.User;

@Projection(name = "userList", types = { User.class })
public interface UserListProjection {
    Long getMatricule(); // Utilisé comme valeur d'identification
    String getFirstName();
    String getLastName();
    String getPhotoUrl();
    Boolean getIsActivated();
    String getGrade();
    String getPosition();
    Long getNote();
}