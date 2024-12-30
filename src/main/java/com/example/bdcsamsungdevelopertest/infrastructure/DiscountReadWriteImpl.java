package com.example.bdcsamsungdevelopertest.infrastructure;

import com.example.bdcsamsungdevelopertest.domain.entity.Discount;
import com.example.bdcsamsungdevelopertest.domain.entity.Product;
import com.example.bdcsamsungdevelopertest.domain.interfaces.DiscountReadWrite;
import com.example.bdcsamsungdevelopertest.infrastructure.jpa.DiscountRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DiscountReadWriteImpl implements DiscountReadWrite {

    private final DiscountRepository discountRepository;

    public DiscountReadWriteImpl(
        DiscountRepository discountRepository
    ) {
        this.discountRepository = discountRepository;
    }

    @Override
    public Optional<Discount> findSpecificDiscount(Product product) {
        return discountRepository.findByProduct(product);
    }

    @Override
    public Optional<Discount> findSpecificDiscount(Long id) {
        return discountRepository.findById(id);
    }

    @Override
    public Discount saveDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    @Override
    public void deleteDiscount(Discount discount) {
        discountRepository.delete(discount);
    }
}
