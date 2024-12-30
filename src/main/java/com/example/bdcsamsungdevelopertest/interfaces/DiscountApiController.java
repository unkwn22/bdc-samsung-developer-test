package com.example.bdcsamsungdevelopertest.interfaces;

import com.example.bdcsamsungdevelopertest.application.DiscountFacade;
import com.example.bdcsamsungdevelopertest.domain.command.DiscountRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.info.DiscountInfo;
import com.example.bdcsamsungdevelopertest.interfaces.dto.DiscountRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discount/api/v1")
public class DiscountApiController {

    private final DiscountFacade discountFacade;

    public DiscountApiController(
        DiscountFacade discountFacade
    ) {
        this.discountFacade = discountFacade;
    }

    /**
     * 새로운 할인 생성
     *
     * 201 created: response: 생성된 상품 정보
     * 400 bad (유효성 검사 관련), response: 상품 정보 없음, 중복 할인
     * */
    @PostMapping("/register")
    public ResponseEntity<DiscountInfo.DiscountEntity> registerDiscount(
        @RequestBody DiscountRequestDto body
    ) {
        DiscountRequestCommand command = new DiscountRequestCommand(
            body.productId(),
            body.discountValue()
        );
        DiscountInfo.DiscountEntity info = discountFacade.requestDiscountRegistration(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(info);
    }

    /**
     * 특정 할인 정보 조회
     * ※ [GET] PathVariable {discountId} Long 타입과 겹쳐서 하위 url에 추가
     *
     * PathVariable {productId} (Long)
     * 200 ok: response: { "productId": 1, "discountValue": 1000 }
     * 404 notfound, response:
     * */
    @GetMapping("/byProduct/{productId}")
    public ResponseEntity<DiscountInfo.DiscountEntity> searchDiscountByProduct(
        @PathVariable("productId") Long productId
    ) {
        DiscountRequestCommand command = new DiscountRequestCommand();
        command.setProductId(productId);
        DiscountInfo.DiscountEntity info = discountFacade.requestDiscountSearchByProduct(command);
        return ResponseEntity.status(HttpStatus.OK)
                .body(info);
    }

    /**
     * 특정 할인 정보 조회
     * ※ [GET] PathVariable {productId} Long 타입과 겹쳐서 하위 url에 추가
     *
     * PathVariable {discountId} (Long)
     * 200 ok: response: { "productId": 1, "discountValue": 1000 }
     * 404 notfound, response:
     * */
    @GetMapping("/byDiscount/{discountId}")
    public ResponseEntity<DiscountInfo.DiscountEntity> searchDiscount(
        @PathVariable("discountId") Long discountId
    ) {
        DiscountRequestCommand command = new DiscountRequestCommand();
        command.setId(discountId);
        DiscountInfo.DiscountEntity info = discountFacade.requestDiscountSearch(command);
        return ResponseEntity.status(HttpStatus.OK)
                .body(info);
    }
}
