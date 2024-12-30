package com.example.bdcsamsungdevelopertest.domain.command;

public class DiscountRequestCommand {
    private Long id;
    private Integer discountValue;
    private Long productId;
    public DiscountRequestCommand(
        Long productId,
        Integer discountValue
    ) {
        this.productId = productId;
        this.discountValue = discountValue;
    }
    public DiscountRequestCommand(Long id) {
        this.id = id;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getDiscountValue() { return discountValue; }
    public void setDiscountValue(Integer discountValue) { this.discountValue = discountValue; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
}
