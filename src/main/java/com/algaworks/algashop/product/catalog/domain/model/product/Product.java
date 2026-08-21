package com.algaworks.algashop.product.catalog.domain.model.product;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "products")
public class Product {

    @Id
    @EqualsAndHashCode.Include
    private UUID uuid;
    private String name;
    private String brand;
    private String description;
    private Integer quantityInStock;
    private Boolean enabled;
    private BigDecimal regularPrice;
    private BigDecimal salePrice;
    @Version
    private Long version;
    @CreatedDate
    private OffsetDateTime createdAt;
    @LastModifiedDate
    private OffsetDateTime updateAt;
    @CreatedBy
    private UUID createdByUserId;
    @LastModifiedBy
    private UUID lastModifiedByUserId;

    @Builder
    public Product(String name, String brand, String description, Boolean enabled, BigDecimal regularPrice, BigDecimal salePrice) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.enabled = enabled;
        this.regularPrice = regularPrice;
        this.salePrice = salePrice;
    }
}
