package com.example.bdcsamsungdevelopertest.domain.service;

import com.example.bdcsamsungdevelopertest.common.exception.BadRequestException;
import com.example.bdcsamsungdevelopertest.domain.command.OrderItemCommand;
import com.example.bdcsamsungdevelopertest.domain.command.OrderItemRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.entity.OrderItem;
import com.example.bdcsamsungdevelopertest.domain.entity.Orders;
import com.example.bdcsamsungdevelopertest.domain.entity.Product;
import com.example.bdcsamsungdevelopertest.domain.info.OrderItemInfo;
import com.example.bdcsamsungdevelopertest.domain.interfaces.OrderItemReadWrite;
import com.example.bdcsamsungdevelopertest.domain.interfaces.ProductReadWrite;
import com.example.bdcsamsungdevelopertest.domain.query.OrdersQueryEnum;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemService {

    private final OrderItemReadWrite orderItemReadWrite;
    private final ProductReadWrite productReadWrite;
    private final ProductService productService;

    public OrderItemService(
        OrderItemReadWrite orderItemReadWrite,
        ProductReadWrite productReadWrite,
        ProductService productService
    ) {
        this.orderItemReadWrite = orderItemReadWrite;
        this.productReadWrite = productReadWrite;
        this.productService = productService;
    }

    @Transactional
    public OrderItemCommand.OrderItemEntity createOrderItem(
        OrderItemRequestCommand registerCommand,
        Orders orders
    ) {
        Product product = productService.productGetOrThrow(
                productReadWrite.findSpecificProduct(registerCommand.getProductId())
        );
        OrderItem beforeSaveOrderItemEntity = new OrderItem(
            registerCommand.getOrderPrice(),
            registerCommand.getQuantity(),
            orders,
            product
        );
        OrderItem savedOrderItem = orderItemReadWrite.saveOrderItem(beforeSaveOrderItemEntity);
        return toOrdersItemEntityCommand(savedOrderItem);
    }

    /**
     * VALIDATION
     * */

    /**
    * 상품 id 추출 및 수량 개수 유효성 확인
    * */
    public List<Long> extractProductIdsAndValidateQuantity(
        List<OrderItemRequestCommand> orderItemsRequestCommand
    ) {
        List<Long> requestProductIds = new ArrayList<>();
        for(OrderItemRequestCommand iterateOrderItemRequestCommand : orderItemsRequestCommand) {
            requestProductIds.add(iterateOrderItemRequestCommand.getProductId());
            validateIfQuantityIsNon(iterateOrderItemRequestCommand.getQuantity());
        }
        return requestProductIds;
    }

    private void validateIfQuantityIsNon(Integer quantity) {
        if(quantity.equals(0)) throw new BadRequestException("수량이 0일 수 없습니다.");
    }

    /**
     * CONSTRUCTOR & METHODS
     * */
    public OrderItemCommand.OrderItemEntity toOrdersItemEntityCommand(
        OrderItem orderItem
    ) {
        return new OrderItemCommand.OrderItemEntity(
            orderItem.getId(),
            orderItem.getOrderPrice(),
            orderItem.getQuantity(),
            orderItem.getOrder().getId(),
            orderItem.getProduct().getId()
        );
    }

    public OrderItemInfo.OrdersEntity toOrdersItemInfo(
        OrderItemCommand.OrderItemEntity orderItemEntityCommand
    ) {
        return new OrderItemInfo.OrdersEntity(
            orderItemEntityCommand.productId(),
            orderItemEntityCommand.quantity()
        );
    }

    public OrderItemInfo.OrdersEntity tupleToOrdersItemInfo(
        Tuple tuple
    ) {
        Long productId = tuple.get(Expressions.numberPath(Long.class, OrdersQueryEnum.PRODUCT_ID.name()));
        Integer quantity = tuple.get(Expressions.numberPath(Integer.class, OrdersQueryEnum.QUANTITY.name()));
        return new OrderItemInfo.OrdersEntity(
            productId,
            quantity
        );
    }
}
