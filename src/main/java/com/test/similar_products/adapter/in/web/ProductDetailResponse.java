package com.test.similar_products.adapter.in.web;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductDetailResponse {

    private String id;

    private String name;

    private Double price;

    private Boolean availability;
}
