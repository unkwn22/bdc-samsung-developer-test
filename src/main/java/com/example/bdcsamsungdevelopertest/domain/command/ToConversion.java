package com.example.bdcsamsungdevelopertest.domain.command;

import com.example.bdcsamsungdevelopertest.domain.entity.*;
import com.example.bdcsamsungdevelopertest.domain.info.*;
import com.example.bdcsamsungdevelopertest.domain.query.OrdersQueryEnum;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.bdcsamsungdevelopertest.common.util.EmailStaticValue.SAMSUNG_EMAIL;

public class ToConversion {

    public static MemberCommand.MemberEntity toMemberEntityCommand(
            Member member
    ) {
        return new MemberCommand.MemberEntity(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getAddress()
        );
    }

    public static MemberInfo.MemberEntity toMemberInfo(
            MemberCommand.MemberEntity memberEntityCommand
    ) {
        return new MemberInfo.MemberEntity(
                memberEntityCommand.name(),
                memberEntityCommand.email() + SAMSUNG_EMAIL,
                memberEntityCommand.address()
        );
    }

    public static List<MemberCommand.MemberEntity> toMemberEntitiesCommand(
            List<Member> members
    ) {
        List<MemberCommand.MemberEntity> command = new ArrayList<>();
        members.forEach( iterateMember ->
                command.add(toMemberEntityCommand(iterateMember))
        );
        return command;
    }

    public static List<MemberInfo.MemberEntity> toMemberInfos(
            List<MemberCommand.MemberEntity> memberEntitiesCommand
    ) {
        List<MemberInfo.MemberEntity> infos = new ArrayList<>();
        memberEntitiesCommand.forEach( iterateMemberCommand ->
                infos.add(toMemberInfo(iterateMemberCommand))
        );
        return infos;
    }

    /**
     * 상품 command 생성자
     *
     * ORDER:
     * 1. 인자로 받은 상품 정보 command로 변환
     * 2. 할인 상품 객체 nullable 객체로 변환
     * 3. 할인 상품이 null이 아닐때 discountService에 toDiscountEntityCommand 생성자로 discount 객체 넘김
     * 4. 상품 command에 discountCommand set
     * */
    public static ProductEntityCommand toProductEntityCommand(
            Product product
    ) {
        ProductEntityCommand command = new ProductEntityCommand(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
        Optional<Discount> discountObject = Optional.ofNullable(product.getDiscount());
        if(discountObject.isPresent()) {
            Discount discount = discountObject.get();
            DiscountCommand.DiscountEntity discountCommand
                    = toDiscountEntityCommand(discount, product.getId());
            command.setCommand(discountCommand);
        }
        return command;
    }

    /**
     * 상품 info 생성자
     *
     * ORDER:
     * 1. 인자로 받은 상품 command를 info로 변환
     * 2. 할인 상품 command nullable 객체로 변환
     * 3. 할인 상품 command가 null이 아닐때 discountService에 toDiscountInfo 생성자로 discountCommand 객체 넘김
     * 4. 상품 info에 discountInfo set
     * */
    public static ProductInfo toProductInfo(
            ProductEntityCommand productEntityCommand
    ) {
        ProductInfo info = new ProductInfo(
                productEntityCommand.getName(),
                productEntityCommand.getPrice()
        );
        Optional<DiscountCommand.DiscountEntity> discountCommandObject
                = Optional.ofNullable(productEntityCommand.getDiscountCommand());
        if(discountCommandObject.isPresent()) {
            DiscountCommand.DiscountEntity discountCommand = discountCommandObject.get();
            DiscountInfo.DiscountEntity discountInfo = toDiscountInfo(discountCommand);
            info.setDiscountInfo(discountInfo);
        }
        return info;
    }

    public static List<ProductEntityCommand> toProductEntitiesCommand(
            List<Product> products
    ) {
        List<ProductEntityCommand> command = new ArrayList<>();
        products.forEach( iterateProduct ->
                command.add(toProductEntityCommand(iterateProduct))
        );
        return command;
    }

    public static List<ProductInfo> toProductInfos(
            List<ProductEntityCommand> productEntitiesCommand
    ) {
        List<ProductInfo> infos = new ArrayList<>();
        productEntitiesCommand.forEach( iterateProductCommand ->
                infos.add(toProductInfo(iterateProductCommand))
        );
        return infos;
    }

    public static Map<Long, ProductEntityCommand> toProductEntitiesCommandMap(
            List<Product> products
    ) {
        return products.stream()
                .collect(
                        Collectors.toMap(
                                Product::getId,
                                ToConversion::toProductEntityCommand
                        )
                );
    }

    public static OrderItemCommand.OrderItemEntity toOrdersItemEntityCommand(
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

    public static OrderItemInfo.OrdersEntity toOrdersItemInfo(
            OrderItemCommand.OrderItemEntity orderItemEntityCommand
    ) {
        return new OrderItemInfo.OrdersEntity(
                orderItemEntityCommand.productId(),
                orderItemEntityCommand.quantity()
        );
    }

    public static OrderItemInfo.OrdersEntity tupleToOrdersItemInfo(
            Tuple tuple
    ) {
        Long productId = tuple.get(Expressions.numberPath(Long.class, OrdersQueryEnum.PRODUCT_ID.name()));
        Integer quantity = tuple.get(Expressions.numberPath(Integer.class, OrdersQueryEnum.QUANTITY.name()));
        return new OrderItemInfo.OrdersEntity(
                productId,
                quantity
        );
    }

    public static OrdersCommand.OrdersEntity toOrdersEntityCommand(
            Orders orders
    ) {
        return new OrdersCommand.OrdersEntity(
                orders.getId(),
                toMemberEntityCommand(orders.getMember()),
                orders.getTotalAmount(),
                orders.getAddress(),
                orders.getOrderedItems().stream()
                        .map(ToConversion::toOrdersItemEntityCommand)
                        .collect(Collectors.toList())
        );
    }

    public static OrdersInfo.OrdersEntity toOrdersInfo(
            OrdersCommand.OrdersEntity ordersEntityCommand
    ) {
        return new OrdersInfo.OrdersEntity(
                ordersEntityCommand.memberCommand().id(),
                ordersEntityCommand.address(),
                ordersEntityCommand.totalAmount(),
                ordersEntityCommand.ordersItemsEntityCommand().stream()
                        .map(ToConversion::toOrdersItemInfo)
                        .collect(Collectors.toList())
        );
    }

    public static List<OrdersInfo.OrdersEntity> toOrdersInfos(
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
                orderItemInfos.add(tupleToOrdersItemInfo(tuple));
            }
            ordersInfos.add(tupleToOrdersInfo(firstTuple, orderItemInfos));
        }

        return ordersInfos;
    }

    public static OrdersInfo.OrdersEntity tupleToOrdersInfo(
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

    public static DiscountCommand.DiscountEntity toDiscountEntityCommand(
            Discount discount,
            Long productId
    ) {
        return new DiscountCommand.DiscountEntity(
                discount.getProduct().getId(),
                discount.getDiscountValue(),
                productId
        );
    }

    public static DiscountInfo.DiscountEntity toDiscountInfo(
            DiscountCommand.DiscountEntity discountEntityCommand
    ) {
        return new DiscountInfo.DiscountEntity(
                discountEntityCommand.productId(),
                discountEntityCommand.discountValue()
        );
    }
}
