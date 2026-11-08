package com.warehouse.flow.dto;

import com.warehouse.flow.model.enums.ItemCategory;
import com.warehouse.flow.model.enums.ItemSubcategory;
import com.warehouse.flow.model.enums.StockStatus;
import com.warehouse.flow.model.enums.UnitOfMeasure;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemRequest {

    @NotBlank(message = "El código SKU o referencia es obligatorio")
    @Size(max = 50, message = "La referencia no puede superar los 50 caracteres")
    private String sku;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String name;

    @NotNull(message = "La categoría es obligatoria")
    private ItemCategory category;

    @NotNull(message = "La subcategoría o familia es obligatoria")
    private ItemSubcategory subcategory;

    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.0", inclusive = true, message = "El stock inicial no puede ser negativo")
    private BigDecimal quantity;

    @NotNull(message = "La unidad de medida es obligatoria")
    private UnitOfMeasure unitOfMeasure;

    @Size(max = 50, message = "El lote no puede superar los 50 caracteres")
    private String batchNumber;

    private LocalDate expirationDate;

    @Size(max = 30, message = "La ubicación no puede superar los 30 caracteres")
    private String location;

    private StockStatus status;
}