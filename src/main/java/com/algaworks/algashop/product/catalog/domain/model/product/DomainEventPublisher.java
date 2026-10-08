package com.algaworks.algashop.product.catalog.domain.model.product;

public interface DomainEventPublisher {

    void publishEvent(Object message);
}
