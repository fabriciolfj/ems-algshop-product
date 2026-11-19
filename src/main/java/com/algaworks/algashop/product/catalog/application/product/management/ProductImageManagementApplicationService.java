package com.algaworks.algashop.product.catalog.application.product.management;


import com.algaworks.algashop.product.catalog.application.product.query.ImageOutput;
import com.algaworks.algashop.product.catalog.application.storage.StorageProvider;
import com.algaworks.algashop.product.catalog.application.utility.Mapper;
import com.algaworks.algashop.product.catalog.domain.model.DomainException;
import com.algaworks.algashop.product.catalog.domain.model.product.Image;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageManagementApplicationService {

    private final ProductRepository productRepository;
    private final StorageProvider storageProvider;
    private final Mapper mapper;

    @CacheEvict(cacheNames = "algashop:products:v1", key = "#productId")
    public ImageOutput create(final UUID productId,
                              final ImageInput imageInput) {

        Objects.requireNonNull(productId);
        Objects.requireNonNull(imageInput);

        Product product = findById(productId);

        if (storageProvider.fileExists(imageInput.getRemoteFileName())) {
            throw new DomainException("image of name %s was not found on storage provider"
                    .formatted(imageInput.getRemoteFileName()));
        }

        if (productRepository.existsByImagesName(imageInput.getRemoteFileName())) {
            throw new DomainException("image %s is already in use".formatted(imageInput.getRemoteFileName()));
        }

        UUID imageId = product.addImage(imageInput.getRemoteFileName());
        productRepository.save(product);

        Image image = product.getImage(imageId).orElseThrow();
        return mapper.convert(image, ImageOutput.class);
    }

    @CacheEvict(cacheNames = "algashop:products:v1", key = "#productId")
    public void delete(UUID productId, UUID imageId) {
        Objects.requireNonNull(productId);
        Objects.requireNonNull(imageId);

        var product = findById(productId);
        var image = findImage(imageId, product);

        product.removeImage(image.getId());
        storageProvider.deleteFile(image.getName());

        productRepository.save(product);
    }

    public void primary(UUID productId, UUID imageId) {
        Objects.requireNonNull(productId);
        Objects.requireNonNull(imageId);

        Product product = findById(productId);
        Image image = findImage(imageId, product);

        product.changeMainImage(image.getId());

        productRepository.save(product);
    }

    private Image findImage(UUID imageId, Product product) {
        return product.getImage(imageId)
                .orElseThrow(() ->
                        new DomainException("image of %s was not found on product %s"
                                .formatted(imageId, product.getId())));
    }

    private Product findById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
