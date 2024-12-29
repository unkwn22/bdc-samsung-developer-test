package com.example.bdcsamsungdevelopertest.domain.service;

import com.example.bdcsamsungdevelopertest.domain.command.DiscountCommand;
import com.example.bdcsamsungdevelopertest.domain.entity.Discount;
import com.example.bdcsamsungdevelopertest.domain.info.DiscountInfo;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {

    /**
     * CONSTRUCTOR & METHODS
     * */
    public DiscountCommand.DiscountEntity toDiscountEntityCommand(
        Discount discount,
        Long productId
    ) {
        return new DiscountCommand.DiscountEntity(
            discount.getProduct().getId(),
            discount.getDiscountValue(),
            productId
        );
    }

    public DiscountInfo.DiscountEntity toDiscountInfo(
        DiscountCommand.DiscountEntity discountEntityCommand
    ) {
        return new DiscountInfo.DiscountEntity(
            discountEntityCommand.productId(),
            discountEntityCommand.discountValue()
        );
    }
}
