package com.manywho.services.s3.services;


import com.manywho.sdk.api.run.elements.type.FileListFilter;
import com.manywho.sdk.services.files.FileHandler;
import com.manywho.sdk.services.files.FileUpload;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.s3.ServiceConfiguration;
import com.manywho.services.s3.managers.FileManager;

import javax.inject.Inject;
import java.util.List;

public class S3FileHandler implements FileHandler<ServiceConfiguration> {
    private final FileManager fileManager;

    @Inject
    public S3FileHandler(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public List<$File> findAll(ServiceConfiguration configuration, FileListFilter fileListFilter, String s) {
        return null;
    }

    @Override
    public $File upload(ServiceConfiguration configuration, String s, FileUpload fileUpload) {
        try {
            return fileManager.uploadFile(configuration, fileUpload);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
