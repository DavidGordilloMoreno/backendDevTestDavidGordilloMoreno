package com.test.similar_products.adapter.in.web;

import com.test.similar_products.domain.model.ProductDetail;
import com.test.similar_products.domain.port.GetSimilarProductsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class SimilarProductsController {

    private final GetSimilarProductsUseCase getSimilarProductsUseCase;

    @GetMapping("/{productId}/similar")
    public ResponseEntity<List<ProductDetailResponse>> getSimilarProducts(
            @PathVariable String productId) {

        List<ProductDetail> similarProducts = getSimilarProductsUseCase.getSimilarProducts(productId);

        List<ProductDetailResponse> response = similarProducts.stream()
                .map(this::mapToResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    private ProductDetailResponse mapToResponse(ProductDetail detail) {
        return ProductDetailResponse.builder()
                .id(detail.getId())
                .name(detail.getName())
                .price(detail.getPrice())
                .availability(detail.isAvailability())
                .build();
    }
}