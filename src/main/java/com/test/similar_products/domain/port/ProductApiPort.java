package com.test.similar_products.domain.port;

import com.test.similar_products.domain.model.ProductDetail;

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
    Optional<ProductDetail> getProductDetailById(String productId);
}
