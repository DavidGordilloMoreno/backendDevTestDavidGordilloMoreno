package com.test.similar_products.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDetail {

    private String id;

    private String name;

    private Double price;

    private boolean availability;
}
