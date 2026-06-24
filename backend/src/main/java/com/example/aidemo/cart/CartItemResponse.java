package com.example.aidemo.cart;

import java.math.BigDecimal;

public record CartItemResponse(
        String itemId,
        String name,
        String imageUrl,
        BigDecimal price,
        int quantity,
        BigDecimal subtotal
) {}
