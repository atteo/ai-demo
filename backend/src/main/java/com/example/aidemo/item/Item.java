package com.example.aidemo.item;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("items")
public class Item {

    @Id
    private UUID id;
    private ItemData data;

    protected Item() {}

    public Item(ItemData data) {
        this.data = data;
    }

    public UUID getId() { return id; }
    public ItemData getData() { return data; }
}
