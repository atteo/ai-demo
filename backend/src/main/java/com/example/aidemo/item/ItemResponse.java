package com.example.aidemo.item;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        String category,
        String imageUrl,
        int stock
) {
    static ItemResponse from(Item item) {
        ItemData d = item.getData();
        return new ItemResponse(item.getId(), d.name(), d.description(),
                d.price(), d.category(), d.imageUrl(), d.stock());
    }
}
