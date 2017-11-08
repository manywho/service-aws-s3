package com.manywho.services.s3.database;

import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.services.database.ReadOnlyDatabase;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.s3.ServiceConfiguration;
import com.manywho.services.s3.managers.FileManager;

import javax.inject.Inject;
import java.util.List;

public class FileDatabase implements ReadOnlyDatabase<ServiceConfiguration, $File> {
    private FileManager fileManager;

    @Inject
    public FileDatabase(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public $File find(ServiceConfiguration serviceConfiguration, String fileId) {
        return fileManager.findFile(serviceConfiguration, fileId);
    }

    @Override
    public List<$File> findAll(ServiceConfiguration serviceConfiguration, ListFilter listFilter) {
        return null;
    }
}
