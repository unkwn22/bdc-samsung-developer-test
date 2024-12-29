package com.example.bdcsamsungdevelopertest.domain.service;

import com.example.bdcsamsungdevelopertest.common.exception.BadRequestException;
import com.example.bdcsamsungdevelopertest.common.exception.NotFoundException;
import com.example.bdcsamsungdevelopertest.domain.command.*;
import com.example.bdcsamsungdevelopertest.domain.entity.Member;
import com.example.bdcsamsungdevelopertest.domain.entity.Orders;
import com.example.bdcsamsungdevelopertest.domain.info.OrderItemInfo;
import com.example.bdcsamsungdevelopertest.domain.info.OrdersInfo;
import com.example.bdcsamsungdevelopertest.domain.interfaces.MemberReadWrite;
import com.example.bdcsamsungdevelopertest.domain.interfaces.OrdersReadWrite;
import com.example.bdcsamsungdevelopertest.domain.query.OrdersQueryEnum;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdersService {

    private final OrdersReadWrite ordersReadWrite;
    private final OrderItemService orderItemService;
    private final MemberReadWrite memberReadWrite;
    private final MemberService memberService;

    public OrdersService(
        OrdersReadWrite ordersReadWrite,
        OrderItemService orderItemService,
        MemberReadWrite memberReadWrite,
        MemberService memberService
    ) {
        this.ordersReadWrite = ordersReadWrite;
        this.orderItemService = orderItemService;
        this.memberReadWrite = memberReadWrite;
        this.memberService = memberService;
    }

    @Transactional
    public OrdersCommand.OrdersEntity createOrdersWithRelations(
        OrdersRequestCommand registerCommand
    ) {
        Member member = memberService.memberGetOrThrow(                                                         // [1] 사용자 객체 조회
                memberReadWrite.findSpecificMember(registerCommand.getUserId())
        );
        Orders beforeSaveOrdersEntity = new Orders(                                                             // [2] Orders저장 전 객체 생성
            member,
            registerCommand.getTotalAmount(),
            registerCommand.getAddress()
        );
        Orders orders = ordersReadWrite.saveOrders(beforeSaveOrdersEntity);                                     // [3] Orders 저장
        Long ordersId = orders.getId();                                                                         // [4] Orders id 추출 후 로컬 변수 선언
        List<OrderItemRequestCommand> orderItemsRequestCommand = registerCommand.getOrderItemsRequestCommand(); // [5] 요청한 OrderItems command 리스트 객체 로컬 변수 선언

        List<OrderItemCommand.OrderItemEntity> orderItemCommand = new ArrayList<>();                            // [6] 반환할 orderItemEntityCommand 리스트 선언
        for(OrderItemRequestCommand orderItemRequestCommand : orderItemsRequestCommand) {                       // [7] 요청 OrderItems command 리스트 만큼 루프
            orderItemRequestCommand.setOrdersId(ordersId);                                                      // [8] 저장 된 Orders Id orderItem command에 set
            orderItemCommand.add(orderItemService.createOrderItem(orderItemRequestCommand, orders));            // [9] OrderItem 저장 후 OrderItemEntityCommand 반환받고 [6]에 추가
        }

        return new OrdersCommand.OrdersEntity(                                                                  // [10] OrdersEntityCommand 객체로 변환 후 반환
            orders.getId(),
            memberService.toMemberEntityCommand(member),
            registerCommand.getTotalAmount(),
            registerCommand.getAddress(),
            orderItemCommand
        );
    }

    @Transactional(readOnly = true)
    public OrdersCommand.OrdersEntity searchOrders(Long id) {
        Optional<Orders> searchedOrdersObject = ordersReadWrite.findSpecificOrders(id);
        Orders orders = ordersGetOrThrow(searchedOrdersObject);
        return toOrdersEntityCommand(orders);
    }

    @Transactional
    public void cancelOrders(Long id) {
        Optional<Orders> searchedOrdersObject = ordersReadWrite.findSpecificOrders(id);
        Orders orders = ordersGetOrThrow(searchedOrdersObject);
        orders.cancelOrder();
    }

    @Transactional(readOnly = true)
    public List<OrdersInfo.OrdersEntity> searchOrders(
        OrdersCommand.SearchList searchListCommand
    ) {
        List<Tuple> tupleResult = ordersReadWrite.customFindOrders(
                searchListCommand.userId(),
                searchListCommand.pageable()
        );
        return toOrdersInfos(tupleResult);
    }

    /**
     * VALIDATION
     * */

    /**
     * 총 금액 및 유효성 검사 메소드
     *
     * DESC: 주요 기능 요약
     * 1. 요청한 상품 id 유효성 검사, 대상: 존재하는 상품 객체
     * 2. 같은 상품 id로 여러 요청을 하였을때 한개의 수량으로 통합 (중복 제거) 후 set
     * 3. 주문 당시 상품 가격 set
     * 4. 총 금액 계산 및 set
    * */
    public OrdersRequestCommand validateRequestAndSetTotalAmount(
        OrdersRequestCommand registerCommand,                               // [0-1] 요천한 Orders와 OrderItem 관련 command
        Map<Long, ProductEntityCommand> resOfProdEntComMapSeWithReqIdsMap   // [0-2] 요청한 OrderItem의 productId로 조회 된 전체 상품 객체 [productId, productCommand]
    ) {
        List<OrderItemRequestCommand> orderItemsRequestCommand = registerCommand.getOrderItemsRequestCommand();         // [1] 로컬 orderItemRequestCommand를 변수로 선언
        long totalAmount = 0L;                                                                                          // [2] 총 금액 초기화

        HashMap<Long, OrderItemRequestCommand> distinctProductOrderItemRequestCommandMap = new HashMap<>();             // [3] 중복 orderItem 요청 방지 [productId, orderItemRequestCommand]

        for(OrderItemRequestCommand orderItemRequestCommand : orderItemsRequestCommand) {                               // [4] 요청한 상품id들의 유효성 검사 필요, 비교 대상은 [0-2]
            Long reqProductId = orderItemRequestCommand.getProductId();                                                 // [5] 요청 상품 id 인스턴스 변수
            Integer reqQuantity = orderItemRequestCommand.getQuantity();                                                // [6] 요청 수량 인스턴스 변수

            if(!resOfProdEntComMapSeWithReqIdsMap.containsKey(reqProductId)) {                                          // [7] [0-2]에 요청 상품id가 없으면 유효하진 않은 상품 요청으로 예외 처리
                throw new BadRequestException("요청한 상품 중 없는 상품이 존재합니다.");
            }

            ProductEntityCommand productCommand = resOfProdEntComMapSeWithReqIdsMap.get(reqProductId);                  // [8] 예외가 발생하지 않았다면 유효한 상품을 요청 한것으로 간주
            Optional<DiscountCommand.DiscountEntity> discountCommandObject = Optional.ofNullable(                       // [9] 상품에 대한 할인 객체 nullable 처리
                    productCommand.getDiscountCommand()
            );

            Integer productPrice = productCommand.getPrice();                                                           // [10] 상품 원조 금액 로컬 변수로 초기화

            if(discountCommandObject.isPresent()) {                                                                     // [11] 할인 객체가 존재하다면 할인 금액을 원조 금액에 반영
                DiscountCommand.DiscountEntity discountCommand = discountCommandObject.get();
                productPrice -= discountCommand.discountValue();
            }

            orderItemRequestCommand.setOrderPrice(productPrice);                                                        // [12] 돌고 있는 해당 요청 orderItemCommand에 상품 금액 set
            if(distinctProductOrderItemRequestCommandMap.containsKey(reqProductId)) {                                   // [13] [3]번에 해당 OrderItemCommand가 존재한다면
                Integer existingQuantity = distinctProductOrderItemRequestCommandMap.get(reqProductId).getQuantity();   // [13-1] 존재하는 수량에서 현재 돌고 있는 command 수량의 합 set
                distinctProductOrderItemRequestCommandMap.get(reqProductId).setQuantity(reqQuantity + existingQuantity);
            } else {
                distinctProductOrderItemRequestCommandMap.put(reqProductId, orderItemRequestCommand);                   // [13-2] 존재하지 않는다면 새로운 map으로 put
            }

            totalAmount = totalAmount + ((long) productPrice * reqQuantity);                                            // [13] 총금액 = 총금액 + (상품가격 * 요청수량)
        }
        registerCommand.setTotalAmount(totalAmount);                                                                    // [14] 모든 loop이 끝나면 총 금액 [0-1]의 totalAmount에다 set
        registerCommand.setOrderItemsRequestCommand(                                                                    // [15] 중복 제거된 OrderItemCommand [3]을 새로 registerCommand에 set
            distinctProductOrderItemRequestCommandMap.values().stream().toList()
        );

        return registerCommand;                                                                                         // [16] [0-1] 반환 재사용
    }

    /**
     * Optional orders 객체 유효검사
     * */
    public Orders ordersGetOrThrow(Optional<Orders> searchedOrdersObject) {
        if(searchedOrdersObject.isEmpty()) throw new NotFoundException("존재하지 않는 주문 정보입니다.");
        return searchedOrdersObject.get();
    }

    /**
     * CONSTRUCTOR & METHODS
     * */
    public OrdersCommand.OrdersEntity toOrdersEntityCommand(
        Orders orders
    ) {
        return new OrdersCommand.OrdersEntity(
            orders.getId(),
            memberService.toMemberEntityCommand(orders.getMember()),
            orders.getTotalAmount(),
            orders.getAddress(),
            orders.getOrderedItems().stream()
                    .map(orderItemService::toOrdersItemEntityCommand)
                    .collect(Collectors.toList())
        );
    }

    public OrdersInfo.OrdersEntity toOrdersInfo(
        OrdersCommand.OrdersEntity ordersEntityCommand
    ) {
        return new OrdersInfo.OrdersEntity(
            ordersEntityCommand.memberCommand().id(),
            ordersEntityCommand.address(),
            ordersEntityCommand.totalAmount(),
            ordersEntityCommand.ordersItemsEntityCommand().stream()
                    .map(orderItemService::toOrdersItemInfo)
                    .collect(Collectors.toList())
        );
    }

    public List<OrdersInfo.OrdersEntity> toOrdersInfos(
        List<Tuple> tupleResult
    ) {
        List<OrdersInfo.OrdersEntity> ordersInfos = new ArrayList<>();

        Map<Object, List<Tuple>> groupedByOrdersId = tupleResult.stream()
                .collect(Collectors.groupingBy( tuple ->
                        tuple.get(Expressions.numberPath(Long.class, OrdersQueryEnum.ORDERS_ID.name())))
                );

        for(Map.Entry<Object, List<Tuple>> entry : groupedByOrdersId.entrySet()) {
            List<Tuple> orderItemList = entry.getValue();
            Tuple firstTuple = orderItemList.getFirst();

            List<OrderItemInfo.OrdersEntity> orderItemInfos = new ArrayList<>();
            for(Tuple tuple : orderItemList) {
                orderItemInfos.add(orderItemService.tupleToOrdersItemInfo(tuple));
            }
            ordersInfos.add(tupleToOrdersInfo(firstTuple, orderItemInfos));
        }

        return ordersInfos;
    }

    public OrdersInfo.OrdersEntity tupleToOrdersInfo(
        Tuple tuple,
        List<OrderItemInfo.OrdersEntity> orderItemInfos
    ) {
        Long userId = tuple.get(Expressions.numberPath(Long.class, OrdersQueryEnum.USER_ID.name()));
        String address = tuple.get(Expressions.stringPath(OrdersQueryEnum.ADDRESS.name()));
        Long totalAmount = tuple.get(Expressions.numberPath(Long.class, OrdersQueryEnum.TOTAL_AMOUNT.name()));
        return new OrdersInfo.OrdersEntity(
            userId,
            address,
            totalAmount,
            orderItemInfos
        );
    }
}
