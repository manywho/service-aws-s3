package com.manywho.services.s3.actions;

import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.sdk.services.files.FileUpload;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.s3.ServiceConfiguration;
import com.manywho.services.s3.managers.FileManager;
import javax.inject.Inject;
import java.io.InputStream;
import java.net.URL;

public class UploadFileFromUrlCommand implements ActionCommand<ServiceConfiguration, UploadFileFromUrl, UploadFileFromUrl.Input, UploadFileFromUrl.Output> {
    private FileManager fileManager;

    @Inject
    public UploadFileFromUrlCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public ActionResponse<UploadFileFromUrl.Output> execute(ServiceConfiguration serviceConfiguration, ServiceRequest serviceRequest, UploadFileFromUrl.Input input) {
        try {
            URL url =  new URL(input.getFileUrl());
            InputStream inputStream = url.openStream();
            FileUpload fileUpload = new FileUpload(inputStream, url.getPath());
            $File file = this.fileManager.uploadFile(serviceConfiguration, fileUpload);
            UploadFileFromUrl.Output output = new UploadFileFromUrl.Output(file);

            return new ActionResponse<>(output);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
