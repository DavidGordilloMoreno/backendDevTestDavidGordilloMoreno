package com.test.similarproducts.domain.service;

import com.test.similarproducts.domain.model.DomainProduct;
import com.test.similarproducts.domain.port.ProductApiPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SimilarProductsServiceTest {

    @Mock
    private ProductApiPort productApiPort;

    @InjectMocks
    private SimilarProductsService similarProductsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private DomainProduct createProduct(String id) {
        return DomainProduct.builder().id(id).name("Prod " + id).price(new BigDecimal("10.0")).availability(true).build();
    }

    @Test
    void shouldReturnDetailsForSimilarProducts() {
        List<String> similarIds = List.of("2", "3", "4");
        DomainProduct prod2 = createProduct("2");
        DomainProduct prod3 = createProduct("3");

        when(productApiPort.getSimilarProductId("1")).thenReturn(similarIds);
        when(productApiPort.getProductDetailById("2")).thenReturn(Optional.of(prod2));
        when(productApiPort.getProductDetailById("3")).thenReturn(Optional.of(prod3));
        when(productApiPort.getProductDetailById("4")).thenReturn(Optional.empty());

        List<DomainProduct> result = similarProductsService.getSimilarProducts("1");

        assertEquals(2, result.size());
        assertTrue(result.contains(prod2));
        assertFalse(result.contains(createProduct("4")));

        verify(productApiPort, times(1)).getSimilarProductId("1");
        verify(productApiPort, times(1)).getProductDetailById("2");
        verify(productApiPort, times(1)).getProductDetailById("3");
        verify(productApiPort, times(1)).getProductDetailById("4");
    }

    @Test
    void shouldReturnEmptyListIfNoSimilarIdsFound() {
        when(productApiPort.getSimilarProductId("5")).thenReturn(List.of());

        List<DomainProduct> result = similarProductsService.getSimilarProducts("5");

        // Assert
        assertTrue(result.isEmpty());
        verify(productApiPort, times(1)).getSimilarProductId("5");
        verify(productApiPort, never()).getProductDetailById(anyString());
    }
}