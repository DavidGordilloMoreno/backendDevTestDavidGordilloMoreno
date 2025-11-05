package com.test.similarproducts.adapter.out.api;

import com.test.similarproducts.domain.model.DomainProduct;
import com.test.similarproducts.domain.port.ProductApiPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;


@Component
@RequiredArgsConstructor
public class ProductApiWebClientAdapter implements ProductApiPort {

    private final WebClient webClient;

    @Value("${external.api.similar.ids.url}")
    private String similarIdsUrl;

    @Value("${external.api.product.detail.url}")
    private String productDetailUrl;

    @Override
    @CircuitBreaker(name = "externalApi", fallbackMethod = "getSimilarProductIdFallback")
    @Retry(name = "externalApi")
    public Mono<List<String>> getSimilarProductId(String productId) {
        return webClient.get()
                .uri(similarIdsUrl, productId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.empty())
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .onErrorResume(WebClientResponseException.class, e -> Mono.just(Collections.emptyList()))
                .defaultIfEmpty(Collections.emptyList());
    }

    public Mono<List<String>> getSimilarProductIdFallback(String productId, Throwable t) {
        System.err.println("Circuit Breaker activated for similar product IDs. Error: " + t.getMessage());
        return Mono.just(Collections.emptyList());
    }


    @Override
    @CircuitBreaker(name = "externalApi", fallbackMethod = "getProductDetailByIdFallback")
    @Retry(name = "externalApi")
    public Mono<DomainProduct> getProductDetailById(String productId) {
        return webClient.get()
                .uri(productDetailUrl, productId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.empty())
                .bodyToMono(ProductDetailDTO.class)
                .map(dto -> DomainProduct.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .price(dto.getPrice())
                        .availability(dto.getAvailability())
                        .build())
                .onErrorResume(WebClientResponseException.class, e -> Mono.empty());
    }

    public Mono<DomainProduct> getProductDetailByIdFallback(String productId, Throwable t) {
        System.err.println("Circuit Breaker activated for product detail. Error: " + t.getMessage());
        return Mono.empty();
    }
}