package com.example.bdcsamsungdevelopertest.domain.service;

import com.example.bdcsamsungdevelopertest.common.exception.BadRequestException;
import com.example.bdcsamsungdevelopertest.domain.command.OrderItemCommand;
import com.example.bdcsamsungdevelopertest.domain.command.OrderItemRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.entity.OrderItem;
import com.example.bdcsamsungdevelopertest.domain.entity.Orders;
import com.example.bdcsamsungdevelopertest.domain.entity.Product;
import com.example.bdcsamsungdevelopertest.domain.interfaces.OrderItemReadWrite;
import com.example.bdcsamsungdevelopertest.domain.interfaces.ProductReadWrite;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.bdcsamsungdevelopertest.domain.command.ToConversion.toOrdersItemEntityCommand;

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
}
