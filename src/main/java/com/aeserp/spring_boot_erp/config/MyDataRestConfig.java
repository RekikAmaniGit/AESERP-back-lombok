package com.aeserp.spring_boot_erp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry; // Importation non nécessaire, sera supprimée

import com.aeserp.spring_boot_erp.entity.Product;
import com.aeserp.spring_boot_erp.entity.ProductCategory;
import com.aeserp.spring_boot_erp.entity.User;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    // 💡 REMARQUE: Nous retirons CorsRegistry, car WebSecurityConfig gère maintenant CORS
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) { 

        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH};

        // Désactiver les actions non supportées pour Product
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));

        // Désactiver les actions non supportées pour ProductCategory
        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
        
        // Désactiver les actions non supportées pour User
        config.getExposureConfiguration()
                .forDomainType(User.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));

    }
}