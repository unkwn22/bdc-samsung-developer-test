package com.example.bdcsamsungdevelopertest.application;

import com.example.bdcsamsungdevelopertest.domain.command.DiscountCommand;
import com.example.bdcsamsungdevelopertest.domain.command.DiscountRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.command.ProductEntityCommand;
import com.example.bdcsamsungdevelopertest.domain.info.DiscountInfo;
import com.example.bdcsamsungdevelopertest.domain.info.ProductInfo;
import com.example.bdcsamsungdevelopertest.domain.service.DiscountService;
import com.example.bdcsamsungdevelopertest.domain.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class DiscountFacade {

    private final DiscountService discountService;
    private final ProductService productService;

    public DiscountFacade(
        DiscountService discountService,
        ProductService productService
    ) {
        this.discountService = discountService;
        this.productService = productService;
    }

    /**
     * 할인 생성 퍼사드
     *
     * DESC: 할인 생성을 위한 메소드
     *
     * ORDER:
     * 1. 요청 금액 유효성 검사
     * 2. 검색된 상품 객체 info로 캡슐화 및 반환
     * */
    public DiscountInfo.DiscountEntity requestDiscountRegistration(
        DiscountRequestCommand registerCommand
    ) {
        DiscountCommand.DiscountEntity discountEntityCommand = discountService.validateAndCreateDiscount(registerCommand);
        return discountService.toDiscountInfo(discountEntityCommand);
    }

    /**
     * 할인 정보 조회 퍼사드
     *
     * DESC: 특정 할인 정보 조회를 위한 집합 메소드
     *
     * ORDER:
     * 1. 상품 정보 조회 command 반환 (command 변환 하면서 discount도 command화 해서 캡슐화)
     * 2. 상품 command로 상품 info 변환
     * 3. 상품 info 추출하여 할인 정보 nullable 확인
     * 4. 반환
     * */
    public DiscountInfo.DiscountEntity requestDiscountSearchByProduct(
        DiscountRequestCommand searchCommand
    ) {
        ProductEntityCommand productEntityCommand = productService.searchProduct(searchCommand.getProductId());
        ProductInfo productInfo = productService.toProductInfo(productEntityCommand);
        discountService.extractAndValidateIfDiscountInfoExists(productInfo);
        return productInfo.getDiscountInfo();
    }

    /**
     * 할인 정보 조회 퍼사드
     *
     * DESC: 특정 할인 정보 조회를 위한 집합 메소드
     *
     * ORDER:
     * 1. 할인 id로 조회 및 command entity 반환
     * 2. command entity로 info 변환 후 반환
     * */
    public DiscountInfo.DiscountEntity requestDiscountSearch(
        DiscountRequestCommand searchCommand
    ) {
        DiscountCommand.DiscountEntity discountEntityCommand = discountService.searchDiscount(searchCommand.getId());
        return discountService.toDiscountInfo(discountEntityCommand);
    }
}
