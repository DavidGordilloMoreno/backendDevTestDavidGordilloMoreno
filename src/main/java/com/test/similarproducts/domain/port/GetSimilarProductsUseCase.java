package com.test.similarproducts.domain.port;

import com.test.similarproducts.domain.model.DomainProduct;

import java.util.List;

public interface GetSimilarProductsUseCase {

    /**
     * Get similar products by product id
     *
     * @param productId the product id
     * @return a list of similar products
     */
    List<DomainProduct> getSimilarProducts(String productId);
}
