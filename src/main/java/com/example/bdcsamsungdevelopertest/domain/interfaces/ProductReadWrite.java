package com.example.bdcsamsungdevelopertest.domain.interfaces;

import com.example.bdcsamsungdevelopertest.domain.entity.Product;

public interface ProductReadWrite {

    /**
     * READ
     * */
    boolean validateIfNameExists(String name);


    /**
     * WRITE
     * */
    Product saveProduct(Product product);
}
