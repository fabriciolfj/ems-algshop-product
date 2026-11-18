package com.algaworks.algashop.product.catalog.application.storage;

import java.net.URL;

public interface StorageProvider {

    URL requestUploadUrl(final FileReference fileReference);

    void deleteFile(final String remoteFileName);

    boolean fileExists(final String remoteFileName);
}
