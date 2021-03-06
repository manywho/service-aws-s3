package com.manywho.services.s3.managers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.common.base.Strings;
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
import java.time.OffsetDateTime;
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

    public $File uploadFile(ServiceConfiguration configuration, String path, FileUpload fileUpload) {
        AmazonS3 s3client = S3ClientFactory.create(configuration);

        try (InputStream inputStream = TikaInputStream.get(fileUpload.getContent())) {
            String mimeType = tika.detect(inputStream);
            String id = UUID.randomUUID().toString();

            // If we're given a resource path, we use that as the key prefix, otherwise we just use the plain ID
            String key;
            if (Strings.isNullOrEmpty(path)) {
                key = id;
            } else {
                key = String.format("%s/%s", path, id);
            }

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(mimeType);
            objectMetadata.setContentDisposition("attachment; filename=\"" + fileUpload.getName() + "\"");

            s3client.putObject(new PutObjectRequest(
                    configuration.getBucketName(),
                    key,
                    inputStream,
                    objectMetadata
            ));

            return new $File(
                    id,
                    null,
                    mimeType,
                    fileUpload.getName(),
                    OffsetDateTime.now(),
                    OffsetDateTime.now(),
                    null,
                    generateSignedUrl(s3client, configuration, key),
                    s3client.getUrl(configuration.getBucketName(), key).toString(),
                    null
            );
        } catch (IOException e) {
            throw new RuntimeException("Could not find the mime type of the uploaded file", e);
        }
    }

    private static String generateSignedUrl(AmazonS3 s3Client, ServiceConfiguration configuration, String key) {
        Date expiresAt = new Date(System.currentTimeMillis() + LINK_EXPIRATION_IN_MS);

        return s3Client.generatePresignedUrl(configuration.getBucketName(), key, expiresAt)
                .toString();
    }
}
