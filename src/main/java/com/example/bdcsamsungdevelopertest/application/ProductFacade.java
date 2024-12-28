package com.example.bdcsamsungdevelopertest.application;

import com.example.bdcsamsungdevelopertest.domain.command.ProductEntityCommand;
import com.example.bdcsamsungdevelopertest.domain.command.ProductRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.info.ProductInfo;
import com.example.bdcsamsungdevelopertest.domain.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductFacade {

    private final ProductService productService;

    public ProductFacade(
        ProductService productService
    ) {
        this.productService = productService;
    }

    /**
     * 상품 생성 퍼사드
     *
     * DESC: 상품 생성을 위한 요청 퍼사드
     *
     * ORDER:
     * 1. 고유 상품 이름 검사
     * 2. 상품 가격 검사
     * 3. 상품 등록
     * 4. 생성된 상품 객체 info로 캡슐화 및 반환
     * */
    public ProductInfo requestProductRegistration(
        ProductRequestCommand registerCommand
    ) {
        productService.validateDuplicateName(registerCommand.getName());
        productService.validatePriceRange(registerCommand.getPrice());
        ProductEntityCommand productEntityCommand = productService.createProduct(registerCommand);
        return productService.toProductInfo(productEntityCommand);
    }
}
