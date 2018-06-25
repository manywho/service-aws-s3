package com.manywho.services.s3.files;


import com.manywho.sdk.api.run.elements.type.FileListFilter;
import com.manywho.sdk.services.files.FileHandler;
import com.manywho.sdk.services.files.FileUpload;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.s3.ServiceConfiguration;
import com.manywho.services.s3.managers.FileManager;

import javax.inject.Inject;
import javax.ws.rs.NotSupportedException;
import java.util.List;

public class S3FileHandler implements FileHandler<ServiceConfiguration> {
    private final FileManager fileManager;

    @Inject
    public S3FileHandler(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public List<$File> findAll(ServiceConfiguration configuration, FileListFilter filter, String path) {
        throw new NotSupportedException("Listing all files is not yet supported in the S3 Service");
    }

    @Override
    public $File upload(ServiceConfiguration configuration, String path, FileUpload fileUpload) {
        return fileManager.uploadFile(configuration, path, fileUpload);
    }
}
