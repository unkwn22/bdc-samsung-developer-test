package com.example.bdcsamsungdevelopertest.domain.entity;

import com.example.bdcsamsungdevelopertest.common.exception.BadRequestException;
import com.example.bdcsamsungdevelopertest.domain.command.ProductRequestCommand;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;

import static com.example.bdcsamsungdevelopertest.common.util.MathUtilExtension.requestIsBiggerThanTarget;

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

    public Product(
        ProductRequestCommand registerCommand
    ) {
        this.name = registerCommand.getName();
        this.price = registerCommand.getPrice();
    }

    /**
     * 상품 정보 수정
     *
     * DESC: 관계 데이터와의 정보 유효성 검사, 동적 수정
     *
     * ORDER:
     * 1. 이름 수정
     * 2. 할인 객체 null 확인
     *  2-1. 할인 객체가 없으면 수정 요청 금액 적용
     *  2-2. 할인 객체가 존재한다면
     *   2-2-1. 수정 요청한 금액이 기존 할인 금액보다 크면 수정
     *   2-2-2. 수정 요청한 금액이 기존 할인 금액보다 작으면 예외
     * */
    public void updateProductWithDiscountPriceValidation(
        ProductRequestCommand updateCommand
    ){
        this.name = updateCommand.getName();
        Optional<Discount> discountObject = Optional.ofNullable(this.discount);
        if(discountObject.isEmpty()) {
            this.price = updateCommand.getPrice();
        } else {
            Discount discount = discountObject.get();
            if(requestIsBiggerThanTarget(updateCommand.getPrice(), discount.discountValue)) this.price = updateCommand.getPrice();
            else throw new BadRequestException("수정 요청하신 상품 금액이 할인 금액보다 작습니다.");
        }
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
    @OneToOne(
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER
    )
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
