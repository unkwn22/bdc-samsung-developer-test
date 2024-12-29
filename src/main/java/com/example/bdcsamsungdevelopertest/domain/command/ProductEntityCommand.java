package com.example.bdcsamsungdevelopertest.domain.command;

public class ProductEntityCommand {
    private Long id;                                    // for update uses only
    private String name;
    private Integer price;
    private DiscountCommand.DiscountEntity discountCommand;

    public ProductEntityCommand(
        Long id,
        String name,
        Integer price
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() { return id; }                  // for update uses only
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public DiscountCommand.DiscountEntity getDiscountCommand() { return discountCommand; }
    public void setCommand(DiscountCommand.DiscountEntity discountCommand) { this.discountCommand = discountCommand; }
}