package mate.academy.bookstoreonline.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.HashSet;
import mate.academy.bookstoreonline.dto.cart.CartItemResponseDto;
import mate.academy.bookstoreonline.dto.cart.CartItemUpdateQuantityDto;
import mate.academy.bookstoreonline.dto.cart.ShoppingCartDto;
import mate.academy.bookstoreonline.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    void setup(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get shopping cart - Should return cart DTO for valid user")
    void getCart_ValidUser_ReturnsShoppingCart() throws Exception {
        ShoppingCartDto expected = new ShoppingCartDto(1L, 1L, Collections.emptySet());
        when(shoppingCartService.getCart(any())).thenReturn(expected);

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Add item to cart - Should return 201 Created and cart DTO")
    void create_ValidDto_ReturnsCreatedCart() throws Exception {
        CartItemResponseDto requestDto = new CartItemResponseDto(1L, 2);
        ShoppingCartDto responseDto = new ShoppingCartDto(1L, 1L, new HashSet<>());

        when(shoppingCartService.save(any(), any())).thenReturn(responseDto);

        mockMvc.perform(post("/cart")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Update item - Should return 200 OK and updated cart DTO")
    void update_ValidIdAndDto_ReturnsUpdatedCart() throws Exception {
        Long itemId = 1L;
        CartItemUpdateQuantityDto updateDto = new CartItemUpdateQuantityDto(5);
        ShoppingCartDto responseDto = new ShoppingCartDto(1L, 1L, Collections.emptySet());

        when(shoppingCartService.update(any(), eq(itemId), any())).thenReturn(responseDto);

        mockMvc.perform(put("/cart/items/" + itemId)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Delete item - Should return 204 No Content")
    void delete_ValidId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/cart/items/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Access cart - User with wrong role should be forbidden")
    void getCart_WrongRole_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Create item - Should return 400 Bad Request for negative quantity")
    void create_InvalidQuantity_ReturnsBadRequest() throws Exception {
        CartItemResponseDto invalidDto = new CartItemResponseDto(1L, -5);

        mockMvc.perform(post("/cart")
                        .content(objectMapper.writeValueAsString(invalidDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
