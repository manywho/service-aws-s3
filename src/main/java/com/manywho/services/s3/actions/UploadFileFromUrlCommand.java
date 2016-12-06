package com.manywho.services.s3.actions;

import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
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
            InputStream inputStream = new URL(input.getFileUrl()).openStream();
            $File file = this.fileManager.uploadFile(serviceConfiguration, inputStream);
            UploadFileFromUrl.Output output = new UploadFileFromUrl.Output(file);

            return new ActionResponse<>(output);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
