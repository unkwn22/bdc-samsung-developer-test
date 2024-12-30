package com.example.bdcsamsungdevelopertest.domain.entity;

import com.example.bdcsamsungdevelopertest.common.exception.BadRequestException;
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

    @Enumerated(EnumType.STRING)
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

    /**
     * SETTERS
     * */
    public void updateDiscountValue(Integer discountValue) {
        if(discountValue < 1 || discountValue > 999) throw new BadRequestException("수정 할 수 없는 할인 금액입니다.");
        this.discountValue = discountValue;
    }

    /**
     * toString
     * */
    @Override
    public String toString() {
        return "Discount{" + "id=" + id + ", discountType=" + discountType + ", discountValue=" + discountValue + '}';
    }
}