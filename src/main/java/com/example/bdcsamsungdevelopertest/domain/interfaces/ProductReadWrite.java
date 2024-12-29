package com.example.bdcsamsungdevelopertest.domain.interfaces;

import com.example.bdcsamsungdevelopertest.domain.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductReadWrite {

    /**
     * READ
     * */
    boolean validateIfNameExists(String name);

    Optional<Product> findSpecificProduct(Long id);

    List<Product> findAllProducts();


    /**
     * WRITE
     * */
    Product saveProduct(Product product);
}
