package com.warehouse.flow.entity;

import com.warehouse.flow.model.enums.ItemCategory;
import com.warehouse.flow.model.enums.ItemSubcategory;
import com.warehouse.flow.model.enums.StockStatus;
import com.warehouse.flow.model.enums.UnitOfMeasure;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El código SKU o referencia es obligatorio")
    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Column(nullable = false, length = 150)
    private String name;

    @NotNull(message = "La categoría principal es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ItemCategory category;

    @NotNull(message = "La subcategoría o familia es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ItemSubcategory subcategory;

    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.0", inclusive = true, message = "El stock no puede ser negativo")
    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    @NotNull(message = "La unidad de medida es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(name = "unit_of_measure", nullable = false, length = 15)
    private UnitOfMeasure unitOfMeasure;

    @Column(name = "batch_number", length = 50)
    private String batchNumber; // Lote de producción/recepción

    @Column(name = "expiration_date")
    private LocalDate expirationDate; // Fecha de caducidad

    @Column(length = 30)
    private String location; // Ej: PASILLO-01-ESTANTE-03-A

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StockStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = StockStatus.AVAILABLE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}