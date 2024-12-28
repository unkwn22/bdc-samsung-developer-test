package com.example.bdcsamsungdevelopertest.interfaces;

import com.example.bdcsamsungdevelopertest.application.ProductFacade;
import com.example.bdcsamsungdevelopertest.domain.command.ProductRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.info.ProductInfo;
import com.example.bdcsamsungdevelopertest.interfaces.dto.ProductRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/api/v1")
public class ProductApiController {

    private final ProductFacade productFacade;

    public ProductApiController(
        ProductFacade productFacade
    ) {
        this.productFacade = productFacade;
    }

    /**
     * 새로운 상품 생성
     *
     * 201 created: response: 생성된 상품 정보
     * 400 bad (유효성 검사 관련), response:
    * */
    @PostMapping("/register")
    public ResponseEntity<ProductInfo> registerProduct(
        @RequestBody ProductRequestDto body
    ) {
        ProductRequestCommand command = new ProductRequestCommand(
            body.name(),
            body.price()
        );
        ProductInfo info = productFacade.requestProductRegistration(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(info);
    }
}
