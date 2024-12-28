package com.example.bdcsamsungdevelopertest.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem extends BaseTime {

    public OrderItem() {}

    public OrderItem(
        Integer orderPrice,
        Integer quantity,
        Orders orders,
        Product product
    ) {
        this.orderPrice = orderPrice;
        this.quantity = quantity;
        this.orders = orders;
        this.product = product;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "orderPrice", nullable = false)
    protected Integer orderPrice;

    @Column(name = "quantity", nullable = false)
    protected Integer quantity;

    /**
     * RELATIONS
     * */
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = Orders.class
    )
    @JoinColumn(name = "orders_id", nullable = false)
    protected Orders orders;

    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = Product.class
    )
    @JoinColumn(name = "product", nullable = false)
    protected Product product;

    /**
     * GETTERS
     * */
    public Long getId() {
        return id;
    }

    public Integer getOrderPrice() {
        return orderPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Orders getOrder() {
        return orders;
    }

    public Product getProduct() {
        return product;
    }

    /**
     * SETTERS
     * */
    public void setOrderPrice(Integer orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * toString
     * */
    @Override
    public String toString() {
        return "OrderItem{" + "id=" + id + ", orderPrice=" + orderPrice + ", quantity=" + quantity + '}';
    }
}
