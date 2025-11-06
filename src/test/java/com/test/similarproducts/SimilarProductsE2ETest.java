package com.test.similarproducts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.Matchers.hasItem;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SimilarProductsE2ETest {

    @Autowired
    private WebTestClient webTestClient;

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        String wireMockUrl = "http://localhost:" + wireMockServer.getPort();
        registry.add("external.api.similar.ids.url", () -> wireMockUrl + "/product/{id}/similarids");
        registry.add("external.api.product.detail.url", () -> wireMockUrl + "/product/{id}");
    }

    @Test
    void shouldReturnSimilarProductsDetails_FilteringNonFound_ReactiveFlow() {
        wireMockServer.stubFor(get(urlEqualTo("/product/1/similarids"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody("[\"2\", \"3\", \"4\"]")));

        wireMockServer.stubFor(get(urlEqualTo("/product/2"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":\"2\", \"name\":\"Prod 2\", \"price\":10.0, \"availability\":true}")));

        wireMockServer.stubFor(get(urlEqualTo("/product/3"))
                .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":\"3\", \"name\":\"Prod 3\", \"price\":20.0, \"availability\":false}")));

        wireMockServer.stubFor(get(urlEqualTo("/product/4"))
                .willReturn(aResponse().withStatus(404)));

        webTestClient.get().uri("/product/1/similar")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[*].id").value(hasItem("2"))
                .jsonPath("$[*].id").value(hasItem("3"));

        wireMockServer.verify(getRequestedFor(urlEqualTo("/product/1/similarids")));
        wireMockServer.verify(getRequestedFor(urlEqualTo("/product/4")));
    }
}