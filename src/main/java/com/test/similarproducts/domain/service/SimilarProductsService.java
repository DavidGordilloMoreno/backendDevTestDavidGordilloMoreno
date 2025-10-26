package com.test.similarproducts.domain.service;

import com.test.similarproducts.domain.model.ProductDetail;
import com.test.similarproducts.domain.port.GetSimilarProductsUseCase;
import com.test.similarproducts.domain.port.ProductApiPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimilarProductsService implements GetSimilarProductsUseCase {

    private final ProductApiPort productApiPort;

    @Override
    public List<ProductDetail> getSimilarProducts(String productId) {

        List<String> similarIds = productApiPort.getSimilarProductId(productId);

        if (similarIds == null || similarIds.isEmpty()) {
            return Collections.emptyList();
        }

        return similarIds.stream()
                .map(productApiPort::getProductDetailById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
