package com.test.similarproducts.adapter.out.api;

import com.test.similarproducts.domain.model.ProductDetail;
import com.test.similarproducts.domain.port.ProductApiPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductApiWebClientAdapter implements ProductApiPort {

    private final WebClient webClient;

    @Value("${external.api.similar.ids.url}")
    private String similarIdsUrl;

    @Value("${external.api.product.detail.url}")
    private String productDetailUrl;

    @Override
    public List<String> getSimilarProductId(String productId) {
        try {
            return webClient.get()
                    .uri(similarIdsUrl, productId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                    .block();

        } catch (WebClientResponseException.NotFound e) {
            return Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException("External API error for similar IDs", e);
        }
    }

    @Override
    public Optional<ProductDetail> getProductDetailById(String productId) {
        try {
            ProductDetailDTO dto = webClient.get()
                    .uri(productDetailUrl, productId)
                    .retrieve()
                    .onStatus(status -> status == HttpStatus.NOT_FOUND, ClientResponse::createException)
                    .bodyToMono(ProductDetailDTO.class)
                    .block();

            return Optional.of(ProductDetail.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .price(dto.getPrice())
                    .availability(dto.getAvailability())
                    .build());

        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw new RuntimeException("External API error for product detail", e);
        }
    }
}
