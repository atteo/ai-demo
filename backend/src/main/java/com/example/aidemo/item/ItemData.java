package com.example.aidemo.item;

import java.math.BigDecimal;

public record ItemData(
        String name,
        String description,
        BigDecimal price,
        String category,
        String imageUrl,
        int stock
) {}
