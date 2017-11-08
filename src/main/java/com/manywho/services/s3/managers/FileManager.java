package com.manywho.services.s3.managers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.manywho.sdk.services.files.FileUpload;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.s3.ServiceConfiguration;
import com.manywho.services.s3.s3.S3ClientFactory;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;

import javax.inject.Inject;
import javax.mail.internet.ContentDisposition;
import javax.mail.internet.ParseException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

public class FileManager {
    final static private Integer LINK_EXPIRATION_IN_MS = 300000;

    private final Tika tika;

    @Inject
    public FileManager(Tika tika) {
        this.tika = tika;
    }

    public $File findFile(ServiceConfiguration configuration, String id) {
        AmazonS3 s3Client = S3ClientFactory.create(configuration);

        ObjectMetadata objectMetadata = s3Client.getObjectMetadata(configuration.getBucketName(), id);

        String fileName;

        try {
            fileName = new ContentDisposition(objectMetadata.getContentDisposition())
                    .getParameter("filename");
        } catch (ParseException e) {
            throw new RuntimeException("Unable to read the content disposition for the file " + id, e);
        }

        return new $File(id, fileName, objectMetadata.getContentType(), generateSignedUrl(s3Client, configuration, id));
    }

    public $File uploadFile(ServiceConfiguration configuration, FileUpload fileUpload) {
        AmazonS3 s3client = S3ClientFactory.create(configuration);

        try (InputStream inputStream = TikaInputStream.get(fileUpload.getContent())) {
            String mimeType = tika.detect(inputStream);
            String id = UUID.randomUUID().toString();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(mimeType);
            objectMetadata.setContentDisposition("attachment; filename=\"" + fileUpload.getName() + "\"");

            s3client.putObject(new PutObjectRequest(
                    configuration.getBucketName(),
                    id,
                    fileUpload.getContent(),
                    objectMetadata
            ));

            return new $File(id, fileUpload.getName(), mimeType, generateSignedUrl(s3client, configuration, id));
        } catch (IOException e) {
            throw new RuntimeException("Could not find the mime type of the uploaded file", e);
        }
    }

    private static String generateSignedUrl(AmazonS3 s3Client, ServiceConfiguration configuration, String id) {
        Date expiresAt = new Date(System.currentTimeMillis() + LINK_EXPIRATION_IN_MS);

        return s3Client.generatePresignedUrl(configuration.getBucketName(), id, expiresAt)
                .toString();
    }
}
