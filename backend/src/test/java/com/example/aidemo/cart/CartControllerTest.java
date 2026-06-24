package com.example.aidemo.cart;

import com.example.aidemo.item.Item;
import com.example.aidemo.item.ItemData;
import com.example.aidemo.item.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
class CartControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @Autowired WebApplicationContext context;
    @Autowired CartRepository cartRepository;
    @Autowired ItemRepository itemRepository;

    MockMvc mvc;
    UUID cartId;
    String itemId;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        cartRepository.deleteAll();
        itemRepository.deleteAll();

        Item item = itemRepository.save(new Item(new ItemData(
                "Test Widget", "A test product", new BigDecimal("9.99"),
                "Electronics", "http://example.com/img.jpg", 10)));
        itemId = item.getId().toString();
        cartId = UUID.randomUUID();
    }

    @Test
    void getCartNotFound() throws Exception {
        mvc.perform(get("/api/cart/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void addItemCreatesCart() throws Exception {
        mvc.perform(post("/api/cart/" + cartId + "/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\":\"" + itemId + "\",\"quantity\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartId.toString()))
                .andExpect(jsonPath("$.items[0].itemId").value(itemId))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.items[0].name").value("Test Widget"))
                .andExpect(jsonPath("$.items[0].price").value(9.99))
                .andExpect(jsonPath("$.items[0].subtotal").value(19.98))
                .andExpect(jsonPath("$.total").value(19.98));
    }

    @Test
    void addSameItemIncrementsQuantity() throws Exception {
        mvc.perform(post("/api/cart/" + cartId + "/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\":\"" + itemId + "\",\"quantity\":1}"))
                .andExpect(status().isOk());

        mvc.perform(post("/api/cart/" + cartId + "/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\":\"" + itemId + "\",\"quantity\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].quantity").value(4))
                .andExpect(jsonPath("$.items.length()").value(1));
    }

    @Test
    void updateQuantity() throws Exception {
        mvc.perform(post("/api/cart/" + cartId + "/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\":\"" + itemId + "\",\"quantity\":1}"))
                .andExpect(status().isOk());

        mvc.perform(put("/api/cart/" + cartId + "/items/" + itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].quantity").value(5))
                .andExpect(jsonPath("$.total").value(49.95));
    }

    @Test
    void removeItem() throws Exception {
        mvc.perform(post("/api/cart/" + cartId + "/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\":\"" + itemId + "\",\"quantity\":1}"))
                .andExpect(status().isOk());

        mvc.perform(delete("/api/cart/" + cartId + "/items/" + itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(0))
                .andExpect(jsonPath("$.total").value(0));
    }

    @Test
    void getCartReturnsEnrichedData() throws Exception {
        mvc.perform(post("/api/cart/" + cartId + "/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\":\"" + itemId + "\",\"quantity\":3}"))
                .andExpect(status().isOk());

        mvc.perform(get("/api/cart/" + cartId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].name").value("Test Widget"))
                .andExpect(jsonPath("$.items[0].quantity").value(3))
                .andExpect(jsonPath("$.total").value(29.97));
    }
}
