package com.example.aidemo.item;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemRepository repository;

    public ItemController(ItemRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ItemResponse> list() {
        return repository.findAll().stream().map(ItemResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> get(@PathVariable UUID id) {
        return repository.findById(id)
                .map(ItemResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
