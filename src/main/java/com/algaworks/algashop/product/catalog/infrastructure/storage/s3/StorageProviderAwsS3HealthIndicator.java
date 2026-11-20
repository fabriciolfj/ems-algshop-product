package com.algaworks.algashop.product.catalog.infrastructure.storage.s3;

import com.algaworks.algashop.product.catalog.application.storage.StorageProvider;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StorageProviderAwsS3HealthIndicator implements HealthIndicator {

    private final StorageProvider storageProvider;

    @Override
    public @Nullable Health health() {
        if (storageProvider.healthCheck()) {
            return Health.up().build();
        }

        return Health.status("DEGRADED").build();
    }
}
