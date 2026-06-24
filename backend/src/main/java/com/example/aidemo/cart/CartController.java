package com.example.aidemo.cart;

import com.example.aidemo.item.ItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    public CartController(CartRepository cartRepository, ItemRepository itemRepository) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable UUID cartId) {
        return cartRepository.findById(cartId)
                .map(this::enrich)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{cartId}/items")
    public CartResponse addItem(@PathVariable UUID cartId, @RequestBody AddItemRequest request) {
        Optional<Cart> existing = cartRepository.findById(cartId);
        boolean isNew = existing.isEmpty();

        List<CartItemData> items = existing
                .map(c -> new ArrayList<>(c.getData().items()))
                .orElse(new ArrayList<>());

        Optional<CartItemData> match = items.stream()
                .filter(i -> i.itemId().equals(request.itemId()))
                .findFirst();

        if (match.isPresent()) {
            items.replaceAll(i -> i.itemId().equals(request.itemId())
                    ? new CartItemData(i.itemId(), i.quantity() + request.quantity())
                    : i);
        } else {
            items.add(new CartItemData(request.itemId(), request.quantity()));
        }

        Cart updated = new Cart(cartId, new CartData(items), isNew);
        cartRepository.save(updated);
        return enrich(updated);
    }

    @PutMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartResponse> updateQuantity(
            @PathVariable UUID cartId,
            @PathVariable String itemId,
            @RequestBody UpdateQuantityRequest request) {
        return cartRepository.findById(cartId).map(cart -> {
            List<CartItemData> items = new ArrayList<>(cart.getData().items());
            items.replaceAll(i -> i.itemId().equals(itemId)
                    ? new CartItemData(i.itemId(), request.quantity())
                    : i);
            Cart updated = new Cart(cartId, new CartData(items), false);
            cartRepository.save(updated);
            return ResponseEntity.ok(enrich(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartResponse> removeItem(
            @PathVariable UUID cartId,
            @PathVariable String itemId) {
        return cartRepository.findById(cartId).map(cart -> {
            List<CartItemData> items = cart.getData().items().stream()
                    .filter(i -> !i.itemId().equals(itemId))
                    .collect(Collectors.toCollection(ArrayList::new));
            Cart updated = new Cart(cartId, new CartData(items), false);
            cartRepository.save(updated);
            return ResponseEntity.ok(enrich(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    private CartResponse enrich(Cart cart) {
        List<CartItemData> cartItems = cart.getData().items();
        List<CartItemResponse> responses = cartItems.stream()
                .map(ci -> itemRepository.findById(UUID.fromString(ci.itemId()))
                        .map(item -> {
                            BigDecimal price = item.getData().price();
                            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(ci.quantity()));
                            return new CartItemResponse(
                                    ci.itemId(), item.getData().name(),
                                    item.getData().imageUrl(), price, ci.quantity(), subtotal);
                        })
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();

        BigDecimal total = responses.stream()
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(cart.getId(), responses, total);
    }

    record AddItemRequest(String itemId, int quantity) {}
    record UpdateQuantityRequest(int quantity) {}
}
