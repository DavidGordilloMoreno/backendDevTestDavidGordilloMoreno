package com.test.similarproducts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


@SpringBootTest
@AutoConfigureMockMvc
class SimilarProductsE2ETest {

    @Autowired
    private MockMvc mockMvc;

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
    void shouldReturnSimilarProductsDetails_FilteringNonFound() throws Exception {
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/product/1/similarids"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[\"2\", \"3\", \"4\"]")));

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/product/2"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":\"2\", \"name\":\"Prod 2\", \"price\":10.0, \"availability\":true}")));

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/product/3"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":\"3\", \"name\":\"Prod 3\", \"price\":20.0, \"availability\":false}")));

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/product/4"))
                .willReturn(WireMock.aResponse().withStatus(404)));

        mockMvc.perform(get("/product/1/similar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("2"))
                .andExpect(jsonPath("$[1].id").value("3"));

        wireMockServer.verify(WireMock.getRequestedFor(WireMock.urlEqualTo("/product/1/similarids")));
        wireMockServer.verify(WireMock.getRequestedFor(WireMock.urlEqualTo("/product/4")));
    }
}
