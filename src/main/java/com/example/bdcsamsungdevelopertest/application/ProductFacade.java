package com.example.bdcsamsungdevelopertest.application;

import com.example.bdcsamsungdevelopertest.domain.command.ProductEntityCommand;
import com.example.bdcsamsungdevelopertest.domain.command.ProductRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.info.ProductInfo;
import com.example.bdcsamsungdevelopertest.domain.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.bdcsamsungdevelopertest.domain.command.ToConversion.toProductInfo;
import static com.example.bdcsamsungdevelopertest.domain.command.ToConversion.toProductInfos;

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
        return toProductInfo(productEntityCommand);
    }

    /**
     * 상품 조회 퍼사드
     *
     * DESC: 특정 상품을 조회 하기 위한 집합 메소드
     *
     * ORDER:
     * 1. 고유 아이디로 상품 검색
     * 2. 검색된 상품 객체 info로 캡슐화 및 반환
     * */
    public ProductInfo requestProductSearch(
        ProductRequestCommand searchCommand
    ) {
        ProductEntityCommand productEntityCommand = productService.searchProduct(searchCommand.getId());
        return toProductInfo(productEntityCommand);
    }

    /**
     * 특정 상품 수정 퍼사드
     *
     * DESC: 영속성 컨텍스트를 이용한 객체 update
     *
     * ORDER:
     * 1. 요청한 수정 금액 유효성 검사
     * 2. 요청한 수정 정보 동적 수정
     * 3. 수정 대상 id로 command 조회
     * 4. command에서 info로 변환 후 반환
     * */
    public ProductInfo requestProductUpdate(
        ProductRequestCommand updateCommand
    ) {
        productService.validatePriceRange(updateCommand.getPrice());
        productService.findProductAndValidateNameThenUpdate(updateCommand);
        ProductEntityCommand productEntityCommand = productService.searchProduct(updateCommand.getId());
        return toProductInfo(productEntityCommand);
    }

    /**
     * 전체 상품 조회 퍼사드
     *
     * DESC: pagination 없는 전체 상품 조회
     *
     * ORDER:
     * 1. 전체 상품 리스트 조회 후 command로 변환
     * 2. 전체 상품 리스트 command를 info 리스트로 변환 후 반환
     * */
    public List<ProductInfo> requestSearchProducts() {
        List<ProductEntityCommand> productEntitiesCommand = productService.searchProducts();
        return toProductInfos(productEntitiesCommand);
    }
}
