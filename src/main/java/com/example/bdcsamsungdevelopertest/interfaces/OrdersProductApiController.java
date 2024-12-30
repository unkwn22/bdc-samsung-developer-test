package com.example.bdcsamsungdevelopertest.interfaces;

import com.example.bdcsamsungdevelopertest.application.OrdersFacade;
import com.example.bdcsamsungdevelopertest.domain.command.OrdersProductRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.entity.Orders;
import com.example.bdcsamsungdevelopertest.domain.info.OrdersProductInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordersProduct/api/v1")
public class OrdersProductApiController {

    private OrdersFacade ordersFacade;

    public OrdersProductApiController(
        OrdersFacade ordersFacade
    ) {
        this.ordersFacade = ordersFacade;
    }

    /**
     *
     * 상품별 주문 고객 목록 조회:
     * 경로 변수: {productId} (Long)
     * 쿼리 매개변수: orderStatus (String, 선택 - "CANCELLED", "ORDERED", "ALL". 기본값은 "ALL") - 각각 주문 취소된 주문, 주문 취소 안 된 주문, 모든 주문을 의미합니다.
     * 응답:
     * 200 OK: 해당 상품을 주문한 고객 목록 (JSON 배열) - 각 고객의 id, name, email 포함, 주문 정보 (주문일시, 주문 수량, 주문 상태) 포함. orderStatus 파라미터에 따라 주문 상태를 필터링하여 반환합니다.
     * 404 Not Found: 상품이 존재하지 않는 경우.
     * ※ pagination 내용은 없음으로 전체 리스트 조회
     *
    * */
    @GetMapping("/{productId}")
    public ResponseEntity<List<OrdersProductInfo.OrdersProduct>> searchOrdersGroup(
        @PathVariable("productId") Long productId,
        // TODO 400 Bad Request: orderStatus 파라미터 값이 유효하지 않은 경우
        @RequestParam(value = "orderStatus", required = false, defaultValue = "ALL") Orders.OrderStatus orderStatus
    ) {
        OrdersProductRequestCommand command = new OrdersProductRequestCommand(
            productId,
            orderStatus
        );
        List<OrdersProductInfo.OrdersProduct> infos = ordersFacade.requestSearchOrderItemsGroup(command);
        return ResponseEntity.status(HttpStatus.OK)
                .body(infos);
    }

}
