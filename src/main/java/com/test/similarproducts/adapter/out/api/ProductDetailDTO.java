package com.test.similarproducts.adapter.out.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailDTO {

    private String id;

    private String name;

    private Double price;

    private Boolean availability;
}
