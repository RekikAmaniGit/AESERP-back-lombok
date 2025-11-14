package com.aeserp.spring_boot_erp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.aeserp.spring_boot_erp.entity.Product;
import com.aeserp.spring_boot_erp.projection.ProductProjection;

@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(excerptProjection = ProductProjection.class)
public interface ProductRepository extends JpaRepository<Product, Long> {

}
