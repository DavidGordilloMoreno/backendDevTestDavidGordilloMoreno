package com.test.similarproducts.adapter.out.api;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDetailDTO {

    private String id;

    private String name;

    private BigDecimal price;

    private Boolean availability;
}
