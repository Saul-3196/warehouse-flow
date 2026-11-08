package com.warehouse.flow.dto;

import com.warehouse.flow.model.enums.ItemCategory;
import com.warehouse.flow.model.enums.ItemSubcategory;
import com.warehouse.flow.model.enums.StockStatus;
import com.warehouse.flow.model.enums.UnitOfMeasure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemResponse {

    private Long id;
    private String sku;
    private String name;
    private ItemCategory category;
    private ItemSubcategory subcategory;
    private BigDecimal quantity;
    private UnitOfMeasure unitOfMeasure;
    private String batchNumber;
    private LocalDate expirationDate;
    private String location;
    private StockStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}