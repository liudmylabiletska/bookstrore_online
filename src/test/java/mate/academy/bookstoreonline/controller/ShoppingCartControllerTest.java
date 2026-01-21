package mate.academy.bookstoreonline.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.bookstoreonline.dto.cart.CartItemRequestDto;
import mate.academy.bookstoreonline.dto.cart.CartItemUpdateQuantityDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setup(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(scripts = "classpath:database/shopping-cart/add-cart-and-items.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shopping-cart/remove-cart-and-items.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get shopping cart - Should return real data from DB")
    void getCart_ValidUser_ReturnsShoppingCart() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.cartItems").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(scripts = "classpath:database/shopping-cart/add-cart-and-items.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shopping-cart/remove-cart-and-items.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Add item to cart - Should persist to real DB")
    void create_ValidDto_ReturnsCreatedCart() throws Exception {
        CartItemRequestDto requestDto = new CartItemRequestDto(1L, 2);
        mockMvc.perform(post("/cart")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cartItems").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(scripts = "classpath:database/shopping-cart/add-cart-and-items.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shopping-cart/remove-cart-and-items.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update item quantity - Should update record in DB")
    void update_ValidIdAndDto_ReturnsUpdatedCart() throws Exception {
        Long cartItemId = 1L;
        CartItemUpdateQuantityDto updateDto = new CartItemUpdateQuantityDto(10);
        mockMvc.perform(put("/cart/items/" + cartItemId)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItems[0].quantity").value(10));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    @DisplayName("Access cart - Forbidden for role ADMIN")
    void getCart_WrongRole_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isForbidden());
    }
}
