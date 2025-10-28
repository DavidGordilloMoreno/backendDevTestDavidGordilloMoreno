package com.test.similar_products.domain.port;

import com.test.similar_products.domain.model.ProductDetail;

import java.util.List;

public interface GetSimilarProductsUseCase {

    /**
     * Get similar products by product id
     *
     * @param productId the product id
     * @return a list of similar products
     */
    List<ProductDetail> getSimilarProducts(String productId);
}
