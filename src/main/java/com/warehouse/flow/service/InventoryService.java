package com.warehouse.flow.service;

import com.warehouse.flow.dto.InventoryItemRequest;
import com.warehouse.flow.dto.InventoryItemResponse;
import com.warehouse.flow.entity.InventoryItem;
import com.warehouse.flow.model.enums.ItemCategory;
import com.warehouse.flow.model.enums.ItemSubcategory;
import com.warehouse.flow.model.enums.StockStatus;
import com.warehouse.flow.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InventoryService {

    private final InventoryItemRepository repository;

    public InventoryService(InventoryItemRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public InventoryItemResponse createItem(InventoryItemRequest request) {
        String cleanSku = request.getSku().toUpperCase().trim();
        if (repository.existsBySku(cleanSku)) {
            throw new IllegalArgumentException("Ya existe un artículo con la referencia: " + cleanSku);
        }

        InventoryItem item = InventoryItem.builder()
                .sku(cleanSku)
                .name(request.getName().trim())
                .category(request.getCategory())
                .subcategory(request.getSubcategory())
                .quantity(request.getQuantity())
                .unitOfMeasure(request.getUnitOfMeasure())
                .batchNumber(request.getBatchNumber())
                .expirationDate(request.getExpirationDate())
                .location(request.getLocation() != null ? request.getLocation().toUpperCase().trim() : null)
                .status(request.getStatus() != null ? request.getStatus() : StockStatus.AVAILABLE)
                .build();

        return mapToResponse(repository.save(item));
    }

    @Transactional(readOnly = true)
    public List<InventoryItemResponse> searchItems(String term) {
        String cleanTerm = term.trim();
        return repository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(cleanTerm, cleanTerm)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InventoryItemResponse> getItemsBySubcategory(ItemSubcategory subcategory) {
        return repository.findBySubcategory(subcategory)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InventoryItemResponse> getItemsByCategory(ItemCategory category) {
        return repository.findByCategory(category)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InventoryItemResponse> getAllItems() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public InventoryItemResponse getItemBySku(String sku) {
        InventoryItem item = repository.findBySku(sku.toUpperCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la referencia: " + sku));
        return mapToResponse(item);
    }

    @Transactional
    public InventoryItemResponse updateStock(String sku, BigDecimal newQuantity) {
        if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        InventoryItem item = repository.findBySku(sku.toUpperCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la referencia: " + sku));

        item.setQuantity(newQuantity);
        if (newQuantity.compareTo(BigDecimal.ZERO) == 0) {
            item.setStatus(StockStatus.DEPLETED);
        } else if (item.getStatus() == StockStatus.DEPLETED) {
            item.setStatus(StockStatus.AVAILABLE);
        }

        return mapToResponse(repository.save(item));
    }

    private InventoryItemResponse mapToResponse(InventoryItem item) {
        return InventoryItemResponse.builder()
                .id(item.getId())
                .sku(item.getSku())
                .name(item.getName())
                .category(item.getCategory())
                .subcategory(item.getSubcategory())
                .quantity(item.getQuantity())
                .unitOfMeasure(item.getUnitOfMeasure())
                .batchNumber(item.getBatchNumber())
                .expirationDate(item.getExpirationDate())
                .location(item.getLocation())
                .status(item.getStatus())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}