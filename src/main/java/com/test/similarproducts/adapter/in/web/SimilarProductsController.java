package com.test.similarproducts.adapter.in.web;

import com.test.similarproducts.api.ProductsApi;
import com.test.similarproducts.api.model.ProductDetail;
import com.test.similarproducts.domain.model.DomainProduct;
import com.test.similarproducts.domain.port.GetSimilarProductsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SimilarProductsController implements ProductsApi {

    private final GetSimilarProductsUseCase getSimilarProductsUseCase;

    @Override
    public ResponseEntity<Set<ProductDetail>> getProductSimilar(String productId) {

        List<DomainProduct> similarProducts = getSimilarProductsUseCase.getSimilarProducts(productId);

        Set<ProductDetail> responseSet = similarProducts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return ResponseEntity.ok(responseSet);
    }

    private ProductDetail mapToResponse(DomainProduct domainProduct) {
        ProductDetail response = new ProductDetail();
        response.setId(domainProduct.getId());
        response.setName(domainProduct.getName());
        response.setPrice(domainProduct.getPrice());
        response.setAvailability(domainProduct.isAvailability());
        return response;
    }
}