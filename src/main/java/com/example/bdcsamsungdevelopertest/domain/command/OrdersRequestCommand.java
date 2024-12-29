package com.example.bdcsamsungdevelopertest.domain.command;

import java.util.List;

public class OrdersRequestCommand {
    private Long id;
    private Long userId;
    private String address;
    private List<OrderItemRequestCommand> orderItemsRequestCommand;
    private Long totalAmount;
    public OrdersRequestCommand(Long id) { this.id = id; }
    public OrdersRequestCommand(
        Long userId,
        String address,
        List<OrderItemRequestCommand> orderItemsRequestCommand
    ) {
        this.userId = userId;
        this.address = address;
        this.orderItemsRequestCommand = orderItemsRequestCommand;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public List<OrderItemRequestCommand> getOrderItemsRequestCommand() { return orderItemsRequestCommand; }
    public void setOrderItemsRequestCommand(List<OrderItemRequestCommand> orderItemsRequestCommand) { this.orderItemsRequestCommand = orderItemsRequestCommand; }
    public Long getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Long totalAmount) { this.totalAmount = totalAmount; }
}
