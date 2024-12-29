package com.example.bdcsamsungdevelopertest.domain.service;

import com.example.bdcsamsungdevelopertest.common.exception.BadRequestException;
import com.example.bdcsamsungdevelopertest.common.exception.NotFoundException;
import com.example.bdcsamsungdevelopertest.domain.command.DiscountCommand;
import com.example.bdcsamsungdevelopertest.domain.command.ProductEntityCommand;
import com.example.bdcsamsungdevelopertest.domain.command.ProductRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.entity.Discount;
import com.example.bdcsamsungdevelopertest.domain.entity.Product;
import com.example.bdcsamsungdevelopertest.domain.info.DiscountInfo;
import com.example.bdcsamsungdevelopertest.domain.info.ProductInfo;
import com.example.bdcsamsungdevelopertest.domain.interfaces.ProductReadWrite;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.bdcsamsungdevelopertest.common.util.StringUtilExtension.validateIfBothContentMatches;

@Service
public class ProductService {

    private final ProductReadWrite productReadWrite;
    private final DiscountService discountService;

    public ProductService(
        ProductReadWrite productReadWrite,
        DiscountService discountService
    ) {
        this.productReadWrite = productReadWrite;
        this.discountService = discountService;
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
     * CONSTRUCTOR & METHODS
     * */

    /**
     * 상품 command 생성자
     *
     * ORDER:
     * 1. 인자로 받은 상품 정보 command로 변환
     * 2. 할인 상품 객체 nullable 객체로 변환
     * 3. 할인 상품이 null이 아닐때 discountService에 toDiscountEntityCommand 생성자로 discount 객체 넘김
     * 4. 상품 command에 discountCommand set
    * */
    public ProductEntityCommand toProductEntityCommand(
        Product product
    ) {
        ProductEntityCommand command = new ProductEntityCommand(
            product.getId(),
            product.getName(),
            product.getPrice()
        );
        Optional<Discount> discountObject = Optional.ofNullable(product.getDiscount());
        if(discountObject.isPresent()) {
            Discount discount = discountObject.get();
            DiscountCommand.DiscountEntity discountCommand
                    = discountService.toDiscountEntityCommand(discount, product.getId());
            command.setCommand(discountCommand);
        }
        return command;
    }

    /**
     * 상품 info 생성자
     *
     * ORDER:
     * 1. 인자로 받은 상품 command를 info로 변환
     * 2. 할인 상품 command nullable 객체로 변환
     * 3. 할인 상품 command가 null이 아닐때 discountService에 toDiscountInfo 생성자로 discountCommand 객체 넘김
     * 4. 상품 info에 discountInfo set
     * */
    public ProductInfo toProductInfo(
        ProductEntityCommand productEntityCommand
    ) {
        ProductInfo info = new ProductInfo(
            productEntityCommand.getName(),
            productEntityCommand.getPrice()
        );
        Optional<DiscountCommand.DiscountEntity> discountCommandObject
                = Optional.ofNullable(productEntityCommand.getDiscountCommand());
        if(discountCommandObject.isPresent()) {
            DiscountCommand.DiscountEntity discountCommand = discountCommandObject.get();
            DiscountInfo.DiscountEntity discountInfo = discountService.toDiscountInfo(discountCommand);
            info.setDiscountInfo(discountInfo);
        }
        return info;
    }
}
