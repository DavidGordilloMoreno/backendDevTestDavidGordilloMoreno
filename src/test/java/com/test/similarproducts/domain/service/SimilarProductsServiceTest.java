package com.test.similarproducts.domain.service;

import com.test.similarproducts.domain.model.DomainProduct;
import com.test.similarproducts.domain.port.ProductApiPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimilarProductsServiceTest {

    @Mock
    private ProductApiPort productApiPort;

    @InjectMocks
    private SimilarProductsService similarProductsService;

    @Test
    void shouldReturnTwoProducts_WhenOneDetailIsNotFound() {
        String productId = "1";
        List<String> similarIds = List.of("2", "3", "4");

        when(productApiPort.getSimilarProductId(productId))
                .thenReturn(Mono.just(similarIds));

        when(productApiPort.getProductDetailById("2"))
                .thenReturn(Mono.just(DomainProduct.builder().id("2").name("Prod 2").build()));
        when(productApiPort.getProductDetailById("3"))
                .thenReturn(Mono.just(DomainProduct.builder().id("3").name("Prod 3").build()));
        when(productApiPort.getProductDetailById("4"))
                .thenReturn(Mono.empty());

        StepVerifier.create(similarProductsService.getSimilarProducts(productId))
                .expectNextMatches(products -> {
                    return products.size() == 2 &&
                            products.stream().anyMatch(p -> p.getId().equals("2")) &&
                            products.stream().anyMatch(p -> p.getId().equals("3"));
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyList_WhenNoSimilarIdsFound() {
        String productId = "1";

        when(productApiPort.getSimilarProductId(productId))
                .thenReturn(Mono.just(List.of()));

        StepVerifier.create(similarProductsService.getSimilarProducts(productId))
                .expectNext(List.of())
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyList_WhenSimilarIdsApiFails() {
        String productId = "1";

        when(productApiPort.getSimilarProductId(productId))
                .thenReturn(Mono.error(new RuntimeException("API Connection Failed")));

        StepVerifier.create(similarProductsService.getSimilarProducts(productId))
                .expectNext(List.of())
                .verifyComplete();
    }
}