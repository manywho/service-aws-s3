package com.manywho.services.s3.actions;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.actions.Action;
import com.manywho.sdk.services.types.system.$File;

@Action.Metadata(name = "Get File", summary = "Get File", uri = "get-file")
public class GetFile {

    public static class Input {
        @Action.Input(name = "File Id", contentType = ContentType.String, required = true)
        private String fileId;

        public String getFileId() {
            return fileId;
        }
    }

    public static class Output {
        @Action.Output(name = "Downloaded File", contentType = ContentType.Object)
        private $File file;

        public Output($File file) {
            this.file = file;
        }
    }
}
