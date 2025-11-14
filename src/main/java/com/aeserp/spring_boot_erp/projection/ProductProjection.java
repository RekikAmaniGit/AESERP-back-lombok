package com.aeserp.spring_boot_erp.projection;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.rest.core.config.Projection;
import com.aeserp.spring_boot_erp.entity.Product;

// Define the projection and map it to the Product entity.
@Projection(name = "productDetails", types = { Product.class })
public interface ProductProjection {
    Long getId(); 
    String getSku();
    String getName();
    String getDescription();
    BigDecimal getUnitPrice();
    String getImageUrl();
    boolean isActive();
    int getUnitsInStock();

    Date getDateCreated();
    Date getLastUpdated();
    
    // You can also expose properties of the associated Category, e.g.,
    // String getCategoryName(); // If Product has a method like getCategory().getName() 
}