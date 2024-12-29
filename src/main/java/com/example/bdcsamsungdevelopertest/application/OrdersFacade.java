package com.example.bdcsamsungdevelopertest.application;

import com.example.bdcsamsungdevelopertest.domain.command.*;
import com.example.bdcsamsungdevelopertest.domain.info.OrdersInfo;
import com.example.bdcsamsungdevelopertest.domain.service.MemberService;
import com.example.bdcsamsungdevelopertest.domain.service.OrderItemService;
import com.example.bdcsamsungdevelopertest.domain.service.OrdersService;
import com.example.bdcsamsungdevelopertest.domain.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrdersFacade {

    private final OrdersService ordersService;
    private final MemberService memberService;
    private final ProductService productService;
    private final OrderItemService orderItemService;

    public OrdersFacade(
        OrdersService ordersService,
        MemberService memberService,
        ProductService productService,
        OrderItemService orderItemService
    ) {
        this.ordersService = ordersService;
        this.memberService = memberService;
        this.productService = productService;
        this.orderItemService = orderItemService;
    }

    /**
     * 주문 생성 퍼사드
     *
     * DESC: 주문 생성을 위한 요청 퍼사드
     *
     * ORDER:
     * 1. 유저 정보 확인 및 entity command 반환
     * 2. orderItemsRequestCommand에서 상품 id 추출 및 동시에 quantity 0인지 검사
     * 3. 요청한 상품 리스트를 조회 및 리스트 결과에 대한 유효성 검사 후 entityCommandMap 반환
     *  3-1: resOfProdEntComMapSeWithReqIds = result of product entity command map searched with requested ids map
     * 4. 요청한 상품 리스트중 유효하지 않는 상품id 유효성 검사 및 totalAmount 계산 후 적용, 전체 orderItemRequestCommand에 orderPrice set 적용
     * 5. Orders와 OrderItems 생성 후 nested OrderEntityCommand로 변화 후 반환
     * 6. info 객체로 변환 후 반환
     * */
    public OrdersInfo.OrdersEntity requestOrdersRegistration(
        OrdersRequestCommand registerCommand
    ) {
        memberService.searchMember(registerCommand.getUserId());
        List<Long> productIds = orderItemService.extractProductIdsAndValidateQuantity(registerCommand.getOrderItemsRequestCommand());
        Map<Long, ProductEntityCommand> resOfProdEntComMapSeWithReqIdsMap = productService.searchProductsAndValidateThenReturnMap(productIds);
        OrdersRequestCommand finalRegisterCommand = ordersService.validateRequestAndSetTotalAmount(
                registerCommand,
                resOfProdEntComMapSeWithReqIdsMap
        );
        OrdersCommand.OrdersEntity ordersEntityCommand = ordersService.createOrdersWithRelations(finalRegisterCommand);
        return ordersService.toOrdersInfo(ordersEntityCommand);
    }

    /**
     * 주문 조회 퍼사드
     *
     * DESC: 특정 주문을 조회 하기 위한 집합 메소드
     *
     * ORDER:
     * 1. 주문 조회 후 entity command로 변환
     * 2. command entity로 info 반환
     * */
    public OrdersInfo.OrdersEntity requestOrdersSearch(
        OrdersRequestCommand searchCommand
    ) {
        OrdersCommand.OrdersEntity ordersEntityCommand = ordersService.searchOrders(searchCommand.getId());
        return ordersService.toOrdersInfo(ordersEntityCommand);
    }

    /**
     * 주문 취소 요청 퍼사드
     *
     * DESC: 특정 주문을 조회 하기 위한 집합 메소드
     *
     * ORDER:
     * 1. 주문 취소 요청
     * */
    public void requestOrdersCancel(OrdersRequestCommand searchCommand) {
        ordersService.cancelOrders(searchCommand.getId());
    }
}
