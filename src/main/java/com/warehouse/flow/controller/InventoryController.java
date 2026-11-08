package com.warehouse.flow.controller;

import com.warehouse.flow.dto.InventoryItemRequest;
import com.warehouse.flow.dto.InventoryItemResponse;
import com.warehouse.flow.model.enums.ItemCategory;
import com.warehouse.flow.model.enums.ItemSubcategory;
import com.warehouse.flow.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InventoryItemResponse> createItem(@Valid @RequestBody InventoryItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createItem(request));
    }

    // Búsqueda por texto (ej: GET /api/v1/inventory/search?query=mix cola)
    @GetMapping("/search")
    public ResponseEntity<List<InventoryItemResponse>> searchItems(@RequestParam String query) {
        return ResponseEntity.ok(service.searchItems(query));
    }

    // Navegación por familia (ej: GET /api/v1/inventory/family/MIX o /family/BOXES)
    @GetMapping("/family/{subcategory}")
    public ResponseEntity<List<InventoryItemResponse>> getItemsBySubcategory(
            @PathVariable ItemSubcategory subcategory) {
        return ResponseEntity.ok(service.getItemsBySubcategory(subcategory));
    }

    // Navegación por categoría macro (ej: GET /api/v1/inventory?category=FINISHED_GOOD)
    @GetMapping
    public ResponseEntity<List<InventoryItemResponse>> getAllItems(
            @RequestParam(required = false) ItemCategory category) {
        if (category != null) {
            return ResponseEntity.ok(service.getItemsByCategory(category));
        }
        return ResponseEntity.ok(service.getAllItems());
    }

    @GetMapping("/{sku}")
    public ResponseEntity<InventoryItemResponse> getItemBySku(@PathVariable String sku) {
        return ResponseEntity.ok(service.getItemBySku(sku));
    }

    // Ajuste directo de stock (ej: PATCH /api/v1/inventory/MIX-CHERRY-01/stock?quantity=150.000)
    @PatchMapping("/{sku}/stock")
    public ResponseEntity<InventoryItemResponse> updateStock(
            @PathVariable String sku,
            @RequestParam BigDecimal quantity) {
        return ResponseEntity.ok(service.updateStock(sku, quantity));
    }
}
