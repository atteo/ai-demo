package com.example.aidemo.item;

import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface ItemRepository extends ListCrudRepository<Item, UUID> {}
