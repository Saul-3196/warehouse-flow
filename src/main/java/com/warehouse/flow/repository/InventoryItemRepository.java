package com.warehouse.flow.repository;

import com.warehouse.flow.entity.InventoryItem;
import com.warehouse.flow.model.enums.ItemCategory;
import com.warehouse.flow.model.enums.ItemSubcategory;
import com.warehouse.flow.model.enums.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    Optional<InventoryItem> findBySku(String sku);

    boolean existsBySku(String sku);

    // Búsqueda libre por texto ("mix cherry", "caja", etc.)
    List<InventoryItem> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(String name, String sku);

    // Filtrado por familia directa (Mix, Cremas, Cajas, etc.)
    List<InventoryItem> findBySubcategory(ItemSubcategory subcategory);

    // Filtrado por categoría macro (Producto terminado, Embalaje, Materia prima)
    List<InventoryItem> findByCategory(ItemCategory category);

    List<InventoryItem> findByStatus(StockStatus status);

    List<InventoryItem> findByLocation(String location);
}