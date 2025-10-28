package com.test.similarproducts.domain.port;

import com.test.similarproducts.domain.model.DomainProduct;

import java.util.List;
import java.util.Optional;

public interface ProductApiPort {

    /**
     * Get similar products by product id
     *
     * @param productId the product id
     * @return a list of strings
     */
    List<String> getSimilarProductId(String productId);

    /**
     * Get a product detail by product id
     *
     * @param productId the product id
     * @return a ProductDetail
     */
    Optional<DomainProduct> getProductDetailById(String productId);
}
