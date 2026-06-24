package com.example.aidemo.cart;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("carts")
public class Cart implements Persistable<UUID> {

    @Id
    private UUID id;
    private CartData data;

    @Transient
    private boolean isNew;

    protected Cart() {}

    public Cart(UUID id, CartData data, boolean isNew) {
        this.id = id;
        this.data = data;
        this.isNew = isNew;
    }

    @Override
    public UUID getId() { return id; }

    @Override
    public boolean isNew() { return isNew; }

    public CartData getData() { return data; }
}
