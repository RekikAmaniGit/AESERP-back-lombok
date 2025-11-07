package com.aeserp.spring_boot_erp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aeserp.spring_boot_erp.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
