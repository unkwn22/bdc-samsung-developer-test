package com.example.bdcsamsungdevelopertest.domain.service;

import com.example.bdcsamsungdevelopertest.common.exception.BadRequestException;
import com.example.bdcsamsungdevelopertest.common.exception.NotFoundException;
import com.example.bdcsamsungdevelopertest.domain.command.DiscountCommand;
import com.example.bdcsamsungdevelopertest.domain.command.DiscountRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.entity.Discount;
import com.example.bdcsamsungdevelopertest.domain.entity.Product;
import com.example.bdcsamsungdevelopertest.domain.info.DiscountInfo;
import com.example.bdcsamsungdevelopertest.domain.interfaces.DiscountReadWrite;
import com.example.bdcsamsungdevelopertest.domain.interfaces.ProductReadWrite;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.bdcsamsungdevelopertest.common.util.MathUtilExtension.requestIsBiggerThanTarget;

@Service
public class DiscountService {

    private final ProductReadWrite productReadWrite;
    private final DiscountReadWrite discountReadWrite;

    public DiscountService(
        ProductReadWrite productReadWrite,
        DiscountReadWrite discountReadWrite
    ) {
        this.productReadWrite = productReadWrite;
        this.discountReadWrite = discountReadWrite;
    }

    /**
     * 할인 정보 생성
     *
     * DESC:
     * 1. 요청 상품 존재 여부 확인
     * 2. 해당 상품에 할인 존재 여부 확인
     * 3. 요청 할인 금액이 해당 상품 금액보다 큰지 확인
     * 4. 생성 저장
     * 5. entityCommand로 변환 후 반환
    * */
    @Transactional
    public DiscountCommand.DiscountEntity validateAndCreateDiscount(
        DiscountRequestCommand registerCommand
    ) {
        Optional<Product> searchedProductObject = productReadWrite.findSpecificProduct(registerCommand.getProductId());
        if(searchedProductObject.isEmpty()) throw new NotFoundException("존재하지 않는 상품 정보입니다.");
        Product product = searchedProductObject.get();
        validateDiscountExist(Optional.ofNullable(product.getDiscount()));
        validateProductPriceComparison(registerCommand.getDiscountValue(), product.getPrice());
        Discount beforeSaveDiscountEntity = new Discount(
            product,
            registerCommand.getDiscountValue()
        );
        Discount savedDiscount = discountReadWrite.saveDiscount(beforeSaveDiscountEntity);
        product.setDiscount(savedDiscount);
        return toDiscountEntityCommand(savedDiscount, product.getId());
    }

    /**
     * VALIDATION
     * */
    public void validateDiscountExist(
        Optional<Discount> searchedDiscountObject
    ) {
        if(searchedDiscountObject.isPresent()) throw new BadRequestException("할인 정보가 존재 합니다.");
    }

    public void validateProductPriceComparison(
        Integer discountPrice,
        Integer productPrice
    ) {
        if(requestIsBiggerThanTarget(discountPrice, productPrice)) throw new BadRequestException("요청하신 할인 금액이 상품 금액보다 급니다.");
    }

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
