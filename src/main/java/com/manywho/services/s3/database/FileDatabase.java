package com.manywho.services.s3.database;

import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.services.database.Database;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.s3.ServiceConfiguration;
import com.manywho.services.s3.managers.FileManager;
import javax.inject.Inject;
import java.util.List;

public class FileDatabase implements Database<ServiceConfiguration, $File> {
    private FileManager fileManager;

    @Inject
    public FileDatabase(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public $File find(ServiceConfiguration serviceConfiguration, String fileId) {
        try {
            return this.fileManager.getFile(serviceConfiguration, fileId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<$File> findAll(ServiceConfiguration serviceConfiguration, ListFilter listFilter) {
        return null;
    }

    @Override
    public $File create(ServiceConfiguration serviceConfiguration, $File file) {
        return null;
    }

    @Override
    public List<$File> create(ServiceConfiguration serviceConfiguration, List<$File> list) {
        return null;
    }

    @Override
    public void delete(ServiceConfiguration serviceConfiguration, $File file) {

    }

    @Override
    public void delete(ServiceConfiguration serviceConfiguration, List<$File> list) {

    }

    @Override
    public $File update(ServiceConfiguration serviceConfiguration, $File file) {
        return null;
    }

    @Override
    public List<$File> update(ServiceConfiguration serviceConfiguration, List<$File> list) {
        return null;
    }
}
