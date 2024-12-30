package com.example.bdcsamsungdevelopertest.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "discount")
public class Discount extends BaseTime {

    public Discount() {}

    public Discount(
        Integer discountValue
    ) {
        this.discountValue = discountValue;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "discountType", nullable = false)
    protected DiscountType discountType = DiscountType.AMOUNT;

    @Column(name = "discountValue", nullable = false)
    protected Integer discountValue;

    /**
     * GETTERS
     * */
    public DiscountType getDiscountType() {
        return discountType;
    }

    public Integer getDiscountValue() {
        return discountValue;
    }

    public enum DiscountType {
        AMOUNT,
    }

    public Long getId() {
        return id;
    }

    /**
     * toString
     * */
    @Override
    public String toString() {
        return "Discount{" + "id=" + id + ", discountType=" + discountType + ", discountValue=" + discountValue + '}';
    }
}