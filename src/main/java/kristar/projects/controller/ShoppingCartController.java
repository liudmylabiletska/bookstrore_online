package kristar.projects.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.nio.file.AccessDeniedException;
import kristar.projects.dto.shoppingcartdto.AddCartItemRequestDto;
import kristar.projects.dto.shoppingcartdto.ShoppingCartResponseDto;
import kristar.projects.dto.shoppingcartdto.UpdateCartItemRequestDto;
import kristar.projects.services.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ShoppingCart management", description = "Endpoints for managing shoppingCarts")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Retrieve user's shopping cart",
            description = "Show shopping cart of user")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ShoppingCartResponseDto getCart(Authentication authentication) {
        return shoppingCartService.getCartForCurrentUser();
    }

    @Operation(summary = "Add book to the shopping cart",
            description = "Adding book to the shopping cart")
    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartResponseDto addToCart(@RequestBody @Valid AddCartItemRequestDto request)
            throws AccessDeniedException {
        return shoppingCartService.addBookToCart(request);
    }

    @Operation(summary = "Update quantity of a book in the shopping cart",
            description = "Updating quantity of a book in the shopping cart")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cart-items/{itemId}")
    public ShoppingCartResponseDto updateItem(@PathVariable Long itemId,
                                              @RequestBody @Valid UpdateCartItemRequestDto request
    ) {
        return shoppingCartService.updateCartItem(itemId, request);
    }

    @Operation(summary = "Remove a book from the shopping cart",
            description = "Removing a book from the shopping cart")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/cart-items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@PathVariable Long itemId) {
        shoppingCartService.removeCartItem(itemId);
    }
}
