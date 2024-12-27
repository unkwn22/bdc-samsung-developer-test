package com.example.bdcsamsungdevelopertest.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "discount")
public class Discount extends BaseTime {

    public Discount() {}

    public Discount(
        Product product,
        Integer discountValue
    ) {
        this.product = product;
        this.discountValue = discountValue;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "discountType", nullable = false)
    protected DiscountType discountType = DiscountType.AMOUNT;

    @Column(name = "discountValue", nullable = false)
    protected Integer discountValue;

    /**
     * RELATIONS
     * */
    @OneToOne(mappedBy = "discount")
    protected Product product;

    /**
     * GETTERS
     * */
    public DiscountType getDiscountType() {
        return discountType;
    }

    public Integer getDiscountValue() {
        return discountValue;
    }

    public Product getProduct() {
        return product;
    }

    public enum DiscountType {
        AMOUNT,
    }
}