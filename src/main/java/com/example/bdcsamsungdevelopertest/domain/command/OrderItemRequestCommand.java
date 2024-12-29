package com.example.bdcsamsungdevelopertest.domain.command;

public class OrderItemRequestCommand {
    private Long id;
    private Integer orderPrice;
    private Integer quantity;
    private Long productId;
    private Long ordersId;
    public OrderItemRequestCommand(
        Long productId,
        Integer quantity
    ) {
        this.productId = productId;
        this.quantity = quantity;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getOrderPrice() { return orderPrice; }
    public void setOrderPrice(Integer orderPrice) { this.orderPrice = orderPrice; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getOrdersId() { return ordersId; }
    public void setOrdersId(Long ordersId) { this.ordersId = ordersId; }
}
