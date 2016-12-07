package com.manywho.services.s3.actions;

import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.s3.ServiceConfiguration;
import com.manywho.services.s3.managers.FileManager;
import javax.inject.Inject;

public class GetFileCommand implements ActionCommand<ServiceConfiguration, GetFile, GetFile.Input, GetFile.Output> {
    private FileManager fileManager;

    @Inject
    public GetFileCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public ActionResponse<GetFile.Output> execute(ServiceConfiguration serviceConfiguration, ServiceRequest serviceRequest, GetFile.Input input) {
        try {

            $File file = this.fileManager.getFile(serviceConfiguration, input.getFileId());
            GetFile.Output output = new GetFile.Output(file);

            return new ActionResponse<>(output);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
