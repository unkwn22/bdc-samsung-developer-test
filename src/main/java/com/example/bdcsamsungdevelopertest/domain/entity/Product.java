package com.example.bdcsamsungdevelopertest.domain.entity;

import jakarta.persistence.*;

import java.util.List;

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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "discount_id", nullable = true)
    protected Discount discount;

    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY,
        targetEntity = OrderItem.class,
        cascade = CascadeType.ALL,
        orphanRemoval = true
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

    public Integer getPrice() {
        return price;
    }

    public Discount getDiscount() {
        return discount;
    }

    public List<OrderItem> getOrderedItems() {
        return orderedItems;
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

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    /**
    * toString
    * */
    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name='" + name + '\'' + ", price=" + price + '}';
    }
}
