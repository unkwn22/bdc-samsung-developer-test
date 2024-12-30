package com.example.bdcsamsungdevelopertest.domain.service;

import com.example.bdcsamsungdevelopertest.common.exception.BadRequestException;
import com.example.bdcsamsungdevelopertest.common.exception.NotFoundException;
import com.example.bdcsamsungdevelopertest.domain.command.ProductEntityCommand;
import com.example.bdcsamsungdevelopertest.domain.command.ProductRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.entity.Product;
import com.example.bdcsamsungdevelopertest.domain.interfaces.ProductReadWrite;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.bdcsamsungdevelopertest.common.util.StringUtilExtension.validateIfBothContentMatches;
import static com.example.bdcsamsungdevelopertest.domain.command.ToConversion.*;

@Service
public class ProductService {

    private final ProductReadWrite productReadWrite;

    public ProductService(
        ProductReadWrite productReadWrite
    ) {
        this.productReadWrite = productReadWrite;
    }

    @Transactional(readOnly = true)
    public boolean searchByName(String name) {
        return productReadWrite.validateIfNameExists(name);
    }

    @Transactional
    public ProductEntityCommand createProduct(
        ProductRequestCommand registerCommand
    ) {
        Product beforeSaveProductEntity = new Product(registerCommand);
        Product savedProduct = productReadWrite.saveProduct(beforeSaveProductEntity);
        return toProductEntityCommand(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductEntityCommand searchProduct(Long id) {
        Optional<Product> searchedProductObject = productReadWrite.findSpecificProduct(id);
        Product product = productGetOrThrow(searchedProductObject);
        return toProductEntityCommand(product);
    }

    /**
    * 정적으로 요청한 상품명 고유성 검사 및 금액과 할인 금액 비교 수정
     *
     * ORDER:
     * 1. id로 수정 대상 상품 찾기
     * 2. 조회된 상품 객체 null 확인
     * 3. 수정 요청한 상품명과 기존 상품명의 변동 감지
     *  3-1. 변동이 감지 되었을때 요청한 상품명이 변동 가능한지, 고유한지 확인
    * */
    @Transactional
    public void findProductAndValidateNameThenUpdate(
        ProductRequestCommand updateCommand
    ) {
        Optional<Product> searchedProductObject = productReadWrite.findSpecificProduct(updateCommand.getId());
        Product product = productGetOrThrow(searchedProductObject);
        boolean bothMatch = validateIfBothContentMatches(product.getName(), updateCommand.getName());
        if(!bothMatch) {
            boolean productNameExist = searchByName(updateCommand.getName());
            if(productNameExist) throw new BadRequestException("수정 요청하신 상품명이 이미 존재합니다.");
        }
        product.updateProductWithDiscountPriceValidation(updateCommand);
    }

    @Transactional(readOnly = true)
    public List<ProductEntityCommand> searchProducts() {
        List<Product> searchedProducts = productReadWrite.findAllProducts();
        return toProductEntitiesCommand(searchedProducts);
    }

    @Transactional(readOnly = true)
    public List<Product> searchProducts(List<Long> ids) {
        return productReadWrite.findAllProducts(ids);
    }

    /**
     * VALIDATION
     * */

    /**
     * 상품명 중복 유효성 검사
     *
     * DESC: 상품명 중복검색 유효성 검사 메소드
     *
     * ORDER:
     * 1. 상품명으로 중복 검색
     * 2.
     * */
    public void validateDuplicateName(String name) {
        boolean nameDupValidation = searchByName(name);
        if(nameDupValidation) throw new BadRequestException("이미 존재하는 상품명 입니다.");
    }

    public void validatePriceRange(Integer price) {
        if(price < 1 || 999 < price) throw new BadRequestException("가격 입력이 잘못 되었습니다.");
    }

    /**
     * Optional product 객체 유효검사
     * */
    public Product productGetOrThrow(Optional<Product> searchedProductObject) {
        if(searchedProductObject.isEmpty()) throw new NotFoundException("존재하지 않는 상품 정보입니다.");
        return searchedProductObject.get();
    }

    /**
    * 요청한 상품 id 유효성 검사 및 검색된 상품 commandMap으로 반환
    * */
    public Map<Long, ProductEntityCommand> searchProductsAndValidateThenReturnMap(
        List<Long> productIds
    ) {
        List<Product> searchedProducts = searchProducts(productIds);                     // [1] 요청된 OrderItemRequestCommand의 상품 id들로 상품 목록 조회
        if(searchedProducts.isEmpty()) throw new BadRequestException("없는 상품들입니다."); // [2] 조회 된 상품 목록이 비어있을 경우 400 예외
        return toProductEntitiesCommandMap(searchedProducts);                            // [3] 조회 된 상품 목록 Map으로 변환 [productId, productEntityCommand]
    }

    /**
     * CONSTRUCTOR & METHODS
     * */
}
