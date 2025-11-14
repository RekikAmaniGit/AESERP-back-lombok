package com.aeserp.spring_boot_erp.controller;

import com.aeserp.spring_boot_erp.dao.UserRepository;
import com.aeserp.spring_boot_erp.entity.User;
import com.aeserp.spring_boot_erp.specifications.UserSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/search/filter")
    public Page<User> filterUsers(
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) Boolean isActivated,
            @RequestParam(required = false) Long note,
            Pageable pageable
    ) {
        Specification<User> spec = UserSpecifications.withFilters(matricule, firstName, position, grade, isActivated, note);
        return userRepository.findAll(spec, pageable);
    }
}