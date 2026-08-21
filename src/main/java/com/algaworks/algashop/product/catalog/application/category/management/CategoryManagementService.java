package com.algaworks.algashop.product.catalog.application.category.management;

import com.algaworks.algashop.product.catalog.application.ResourceNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.category.Category;
import com.algaworks.algashop.product.catalog.domain.model.category.CategoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryManagementService {

    private final CategoryRepository categoryRepository;

    public UUID create(@Valid final CategoryInput input) {
        final var category = new Category(input.getName(), input.getEnabled());
        categoryRepository.save(category);

        return category.getId();
    }

    public void update(UUID categoryId, CategoryInput input) {
        final var category = categoryRepository.findById(categoryId)
                .orElseThrow(ResourceNotFoundException::new);

        category.setName(input.getName());
        category.setEnabled(input.getEnabled());

        categoryRepository.save(category);
    }

    public void disable(UUID categoryId) {
        final var category = categoryRepository.findById(categoryId)
                .orElseThrow(ResourceNotFoundException::new);

        category.setEnabled(false);

        categoryRepository.save(category);
    }
}
