package com.example.bdcsamsungdevelopertest.application;

import com.example.bdcsamsungdevelopertest.domain.command.DiscountCommand;
import com.example.bdcsamsungdevelopertest.domain.command.DiscountRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.info.DiscountInfo;
import com.example.bdcsamsungdevelopertest.domain.service.DiscountService;
import org.springframework.stereotype.Service;

@Service
public class DiscountFacade {

    private final DiscountService discountService;

    public DiscountFacade(
        DiscountService discountService
    ) {
        this.discountService = discountService;
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
}
