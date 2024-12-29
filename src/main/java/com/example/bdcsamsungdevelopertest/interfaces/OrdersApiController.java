package com.example.bdcsamsungdevelopertest.interfaces;

import com.example.bdcsamsungdevelopertest.application.OrdersFacade;
import com.example.bdcsamsungdevelopertest.common.exception.BadRequestException;
import com.example.bdcsamsungdevelopertest.common.response.CommonResponse;
import com.example.bdcsamsungdevelopertest.common.util.PageableExtension;
import com.example.bdcsamsungdevelopertest.domain.command.OrderItemRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.command.OrdersCommand;
import com.example.bdcsamsungdevelopertest.domain.command.OrdersRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.info.OrdersInfo;
import com.example.bdcsamsungdevelopertest.interfaces.dto.OrdersRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders/api/v1")
public class OrdersApiController {

    private final OrdersFacade ordersFacade;

    public OrdersApiController(
        OrdersFacade ordersFacade
    ) {
        this.ordersFacade = ordersFacade;
    }

    /**
     * 새로운 주문 생성
     *
     * ※ totalAmount (Long): 할인이 적용된 후의 총 주문 금액
     * ※ 주문 생성 이후 Product, Discount가 변경된다고 하더라도 해당 정보는 반영되지 않습니다.
     * ※ Quantity 개수가 최종적으로 계산되고 oders가 등록 되면 Product
     *
     * 201 created: response:
     * { Orders, OrderItem: []}
     *
     * 400 bad (유효성 검사 관련), response:
     * 예외 조건
     * 1. 사용자/상품 정보 없음
     * 2. quantity 요청 정보가 0인 경우
     * 3. 요청 productId가 중복일 경우 합을 더해서 현재 재고와 맞는지 확인 (예외라기 보다는 프로세스 기준?)
     * */
    @PostMapping("/register")
    public ResponseEntity<OrdersInfo.OrdersEntity> registerOrder(
        @RequestBody OrdersRequestDto.Orders body
    ) {
        OrdersRequestCommand command = new OrdersRequestCommand(
            body.userId(),
            body.address(),
            toOrderItemsCommand(body.orderItems())
        );
        OrdersInfo.OrdersEntity info = ordersFacade.requestOrdersRegistration(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(info);
    }

    /**
     * 특정 주문 정보 조회
     *
     * 200 OK: response: { userId, orderItems:[], address, totalPrice }
     * 404 not found, response:
     * */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrdersInfo.OrdersEntity> searchOrders(
        @PathVariable("orderId") Long id
    ) {
        OrdersRequestCommand command = new OrdersRequestCommand(id);
        OrdersInfo.OrdersEntity info = ordersFacade.requestOrdersSearch(command);
        return ResponseEntity.status(HttpStatus.OK)
                .body(info);
    }

    /**
     * 특정 주문 취소
     *
     * 200 OK: response: { 성공 메세지 }
     * 400 bad, response: 이미 취소
     * 404 not found, response: 요청 주문이 없음
     * */
    @PutMapping("/{orderId}")
    public ResponseEntity<CommonResponse> cancelOrders(
        @PathVariable("orderId") Long id
    ) {
        OrdersRequestCommand command = new OrdersRequestCommand(id);
        ordersFacade.requestOrdersCancel(command);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse("취소 성공"));
    }

    /**
     * 특정 사용자 주문목록 조회
     *
     * ※ 반환 정보 중 주문 번호 (ordersId) 가 없음으로 사용자의 상품 목록 리스트 조회
     *
     * 200 OK: response: [{ userId, orderItems:[], address, totalPrice }]
     * 404 not found, response: 사용자가 없음
     * */
    @GetMapping("/search")
    public ResponseEntity<List<OrdersInfo.OrdersEntity>>
    searchOrdersList(
        @RequestParam(value = "userId", required = true) Long userId,
        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        OrdersCommand.SearchList command = new OrdersCommand.SearchList(
            userId,
            PageableExtension.toPageable(page, size, "desc")
        );
        List<OrdersInfo.OrdersEntity> infos = ordersFacade.requestOrdersListSearch(command);
        return ResponseEntity.status(HttpStatus.OK)
                .body(infos);
    }


    private List<OrderItemRequestCommand> toOrderItemsCommand(
        List<OrdersRequestDto.OrderItem> orderItemDtoList
    ) {
        if(orderItemDtoList.isEmpty()) throw new BadRequestException("등록할 주문 상품이 없습니다.");
        return orderItemDtoList.stream()
                .map( orderItemDto ->
                        new OrderItemRequestCommand(
                                orderItemDto.productId(),
                                orderItemDto.quantity()
                        )
                )
                .collect(Collectors.toList());
    }
}
