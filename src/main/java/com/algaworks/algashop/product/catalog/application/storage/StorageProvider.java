package com.algaworks.algashop.product.catalog.application.storage;

import java.net.URL;

public interface StorageProvider {

    boolean healthCheck();

    URL requestUploadUrl(final FileReference fileReference);

    void deleteFile(final String remoteFileName);

    boolean fileExists(final String remoteFileName);
}
