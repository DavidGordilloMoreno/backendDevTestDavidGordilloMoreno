package com.test.similarproducts.domain.port;

import com.test.similarproducts.domain.model.DomainProduct;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GetSimilarProductsUseCase {

    /**
     * Get similar products by product id
     *
     * @param productId the product id
     * @return a Mono emitting a list of similar products
     */
    Mono<List<DomainProduct>> getSimilarProducts(String productId);
}