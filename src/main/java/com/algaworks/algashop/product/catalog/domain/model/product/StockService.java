package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.domain.model.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StockService {

    private final QuantityInStockAdjustment quantityInStockAdjustment;
    private final DomainEventPublisher domainEventPublisher;

    public StockMovement restock(final Product product, int quantity) {
        Objects.requireNonNull(product);

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        QuantityInStockAdjustment.Result result;
        try {
            result = quantityInStockAdjustment.increase(product.getId(), quantity);
        } catch (Exception e) {
            throw new DomainException(String.format("failed to restock product %s stock", product.getId()));
        }

        if (result.inRestocked()) {
            domainEventPublisher.publishEvent(
                    ProductRestockEvent.builder()
                            .productId(product.getId())
                            .build()
            );
        }

        return StockMovement.builder()
                .productId(product.getId())
                .movementQuantity(quantity)
                .previousQuantity(result.previousQuantity())
                .newQuantity(result.newQuantity())
                .type(StockMovement.MovementType.STOCK_IN)
                .build();
    }

    public StockMovement withdraw(final Product product, int quantity) {
        Objects.requireNonNull(product);

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        QuantityInStockAdjustment.Result result;
        try {
            result = quantityInStockAdjustment.decrease(product.getId(), quantity);
        } catch (Exception e) {
            throw new DomainException(String.format("failed to withdraw product %s stock", product.getId()));
        }

        if (result.isOutOfStock()) {
            domainEventPublisher.publishEvent(ProductSoldOutEvent.builder()
                    .productId(product.getId())
                    .build());
        }

        return StockMovement.builder()
                .productId(product.getId())
                .movementQuantity(quantity)
                .previousQuantity(result.previousQuantity())
                .newQuantity(result.newQuantity())
                .type(StockMovement.MovementType.STOCK_OUT)
                .build();
    }
}
