package com.test.similarproducts.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DomainProduct {

    private String id;

    private String name;

    private BigDecimal price;

    private boolean availability;
}
