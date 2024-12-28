package com.example.bdcsamsungdevelopertest.domain.info;

public class ProductInfo {
    private final String name;
    private final Integer price;
    private DiscountInfo.DiscountEntity discountInfo;
    public ProductInfo(
        String name,
        Integer price
    ) {
        this.name = name;
        this.price = price;
    }
    public String getName() { return name; }
    public Integer getPrice() { return price;}
    public DiscountInfo.DiscountEntity getDiscountInfo() { return discountInfo; }
    public void setDiscountInfo(DiscountInfo.DiscountEntity discountInfo) { this.discountInfo = discountInfo; }
}
