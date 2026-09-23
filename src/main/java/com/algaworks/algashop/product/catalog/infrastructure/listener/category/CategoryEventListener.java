package com.algaworks.algashop.product.catalog.infrastructure.listener.category;

import com.algaworks.algashop.product.catalog.application.category.events.CategoryUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CategoryEventListener {

    @EventListener
    public void handle(final CategoryUpdatedEvent event) {
        log.info("Category received: {}", event.getCategoryId());
    }
}
