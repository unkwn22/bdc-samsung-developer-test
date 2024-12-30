package com.example.bdcsamsungdevelopertest.interfaces;

import com.example.bdcsamsungdevelopertest.application.DiscountFacade;
import com.example.bdcsamsungdevelopertest.domain.command.DiscountRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.info.DiscountInfo;
import com.example.bdcsamsungdevelopertest.interfaces.dto.DiscountRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
