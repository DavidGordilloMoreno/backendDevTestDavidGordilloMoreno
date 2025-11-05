package com.test.similarproducts.domain.service;

import com.test.similarproducts.domain.model.DomainProduct;
import com.test.similarproducts.domain.port.GetSimilarProductsUseCase;
import com.test.similarproducts.domain.port.ProductApiPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SimilarProductsService implements GetSimilarProductsUseCase {

    private final ProductApiPort productApiPort;

    @Override
    public Mono<List<DomainProduct>> getSimilarProducts(String productId) {
        return productApiPort.getSimilarProductId(productId)
                .onErrorReturn(Collections.emptyList())
                .flatMapMany(Flux::fromIterable)
                .flatMap(productApiPort::getProductDetailById)
                .filter(Objects::nonNull)
                .collectList();
    }
}
