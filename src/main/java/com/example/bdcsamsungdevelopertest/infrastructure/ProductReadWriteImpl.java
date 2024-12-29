package com.example.bdcsamsungdevelopertest.infrastructure;

import com.example.bdcsamsungdevelopertest.domain.entity.Product;
import com.example.bdcsamsungdevelopertest.domain.interfaces.ProductReadWrite;
import com.example.bdcsamsungdevelopertest.infrastructure.jpa.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductReadWriteImpl implements ProductReadWrite {

    private final ProductRepository productRepository;

    public ProductReadWriteImpl(
        ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean validateIfNameExists(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public Optional<Product> findSpecificProduct(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
