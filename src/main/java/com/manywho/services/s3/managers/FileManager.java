package com.manywho.services.s3.managers;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.s3.ServiceConfiguration;
import javax.inject.Inject;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Date;
import java.util.UUID;

public class FileManager {
    final static private Integer MAX_TIME_LINK_AVAILABLE = 300000;

    @Inject
    public FileManager() {
    }

    public $File uploadFile(ServiceConfiguration serviceConfiguration, InputStream inputStream) throws Exception {
        BasicAWSCredentials credentials = new BasicAWSCredentials(serviceConfiguration.getAwsAccessKeyId(), serviceConfiguration.getAwsSecretAccessKey());
        AmazonS3 s3client = new AmazonS3Client(credentials);
        String id = UUID.randomUUID().toString();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        s3client.putObject(new PutObjectRequest(serviceConfiguration.getBucketName(), id, inputStream, objectMetadata));
        String mimeType = URLConnection.guessContentTypeFromStream(inputStream);

        return new $File(id, id, mimeType, getFileUrl(serviceConfiguration,id));
    }

    public $File getFile(ServiceConfiguration serviceConfiguration, String id) {
        return new $File(id,id, null, getFileUrl(serviceConfiguration, id));
    }

    private String getFileUrl(ServiceConfiguration serviceConfiguration, String fileId) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(serviceConfiguration.getAwsAccessKeyId(), serviceConfiguration.getAwsSecretAccessKey());
        AmazonS3 s3client = new AmazonS3Client(credentials);
        return s3client.generatePresignedUrl(serviceConfiguration.getBucketName(), fileId, new Date(System.currentTimeMillis() + MAX_TIME_LINK_AVAILABLE)).toString();
    }
}
