package com.example.bdcsamsungdevelopertest.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Orders extends BaseTime {

    public Orders() {}

    public Orders(
        String name,
        Member member,
        Long totalAmount,
        String address
    ) {
        this.name = name;
        this.member = member;
        this.totalAmount = totalAmount;
        this.address = address;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "orderDate", nullable = false, updatable = false)
    protected LocalDateTime orderDate = LocalDateTime.now();

    @Column(name = "totalAmount", nullable = false)
    protected Long totalAmount;

    @Column(name = "address", nullable = false)
    protected String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "orderStatus", nullable = false)
    protected OrderStatus orderStatus = OrderStatus.ORDERED;

    /**
     * RELATIONS
     * */
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = Member.class
    )
    @JoinColumn(name = "member_id", nullable = false)
    protected Member member;

    @OneToMany(
        mappedBy = "orders",
        fetch = FetchType.LAZY,
        targetEntity = OrderItem.class
    )
    protected List<OrderItem> orderedItems;

    /**
     * GETTERS
     * */
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public String getAddress() {
        return address;
    }

    public OrderStatus getOrdersStatus() {
        return orderStatus;
    }

    public Member getMember() {
        return member;
    }

    /**
     * SETTERS
     * */
    public void setName(String name) {
        this.name = name;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOrdered() {
        this.orderStatus = OrderStatus.ORDERED;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCELLED;
    }

    public enum OrderStatus {
        ORDERED,
        CANCELLED,
    }
}