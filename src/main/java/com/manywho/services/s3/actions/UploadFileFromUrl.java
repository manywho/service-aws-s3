package com.manywho.services.s3.actions;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.actions.Action;
import com.manywho.sdk.services.types.system.$File;

@Action.Metadata(name = "Upload File from URL", summary = "Upload File from URL", uri = "file-from-url")
public class UploadFileFromUrl {

    public static class Input {
        @Action.Input(name = "File URL", contentType = ContentType.String, required = true)
        private String fileUrl;

        public String getFileUrl() {
            return fileUrl;
        }
    }

    public static class Output {
        @Action.Output(name = "Uploaded File", contentType = ContentType.Object)
        private $File pdfFile;

        public Output($File pdfFile) {
            this.pdfFile = pdfFile;
        }
    }
}
