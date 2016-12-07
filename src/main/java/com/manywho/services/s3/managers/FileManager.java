package com.manywho.services.s3.managers;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.manywho.sdk.services.files.FileUpload;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.s3.ServiceConfiguration;
import javax.inject.Inject;
import javax.mail.internet.ContentDisposition;
import javax.mail.internet.ParseException;
import java.net.URLConnection;
import java.util.Date;
import java.util.UUID;

public class FileManager {
    final static private Integer MAX_TIME_LINK_AVAILABLE = 300000;

    @Inject
    public FileManager() {
    }

    public $File uploadFile(ServiceConfiguration serviceConfiguration, FileUpload fileUpload) throws Exception {
        AmazonS3 s3client = getS3Client(serviceConfiguration);
        String mimeType = URLConnection.guessContentTypeFromStream(fileUpload.getContent());
        String id = UUID.randomUUID().toString();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(mimeType);
        objectMetadata.setContentDisposition("filename=" + fileUpload.getName());
        s3client.putObject(new PutObjectRequest(serviceConfiguration.getBucketName(), id, fileUpload.getContent(), objectMetadata));

        return new $File(id, fileUpload.getName(), mimeType, getFileUrl(s3client, serviceConfiguration,id));
    }

    public $File getFile(ServiceConfiguration serviceConfiguration, String id) {
        AmazonS3 s3Client = getS3Client(serviceConfiguration);
        S3Object object = s3Client.getObject(serviceConfiguration.getBucketName(), id);
        ObjectMetadata objectMetadata = object.getObjectMetadata();
        String fileName = "";

        try {
            ContentDisposition contentDisposition = new ContentDisposition(objectMetadata.getContentDisposition());
            fileName = contentDisposition.getParameter("filename");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new $File(id, fileName, objectMetadata.getContentType(), getFileUrl(s3Client, serviceConfiguration, id));
    }

    private String getFileUrl(AmazonS3 s3Client, ServiceConfiguration serviceConfiguration, String fileId) {
        return s3Client.generatePresignedUrl(serviceConfiguration.getBucketName(), fileId, new Date(System.currentTimeMillis() + MAX_TIME_LINK_AVAILABLE)).toString();
    }

    private AmazonS3 getS3Client(ServiceConfiguration serviceConfiguration) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(serviceConfiguration.getAwsAccessKeyId(), serviceConfiguration.getAwsSecretAccessKey());
        return new AmazonS3Client(credentials);
    }
}
