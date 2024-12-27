package com.example.bdcsamsungdevelopertest.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product extends BaseTime {

    public Product() {}

    public Product(
        String name,
        Integer price
    ) {
        this.name = name;
        this.price = price;
    }

    public Product(
        String name,
        Integer price,
        Discount discount
    ) {
        this.name = name;
        this.price = price;
        this.discount = discount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "price", nullable = false)
    protected Integer price;

    /**
     * RELATIONS
     * */
    @OneToOne
    @JoinColumn(name = "discount_id", nullable = true)
    protected Discount discount;

    /**
     * GETTERS
     * */
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Discount getDiscount() {
        return discount;
    }

    /**
     * SETTERS
     * */
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
