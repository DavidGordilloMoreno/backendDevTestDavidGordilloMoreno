package com.test.similarproducts.domain.port;

import com.test.similarproducts.domain.model.DomainProduct;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductApiPort {

    /**
     * Get similar products by product id
     *
     * @param productId the product id
     * @return a Mono emitting a list of strings
     */
    Mono<List<String>> getSimilarProductId(String productId);

    /**
     * Get a product detail by product id
     *
     * @param productId the product id
     * @return a Mono emitting a DomainProduct
     */
    Mono<DomainProduct> getProductDetailById(String productId);
}
