package com.ai.document.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class S3StorageService {

    private final S3Client s3Client;

    @Value("${AWS_S3_BUCKET_NAME}")
    private String bucketName;

    public S3StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    // =========================================================
    // UPLOAD FILE TO S3
    // =========================================================

    public String uploadFile(
            MultipartFile file,
            Long organizationId,
            Long userId) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "File cannot be empty"
            );
        }

        String originalFileName =
                file.getOriginalFilename();

        if (originalFileName == null ||
                originalFileName.isBlank()) {

            throw new IllegalArgumentException(
                    "File name is invalid"
            );
        }

        // Remove possible path information
        String cleanFileName =
                originalFileName
                        .replace("\\", "/")
                        .substring(
                                originalFileName
                                        .replace("\\", "/")
                                        .lastIndexOf("/") + 1
                        );

        // Generate unique S3 key
        String s3Key =
                "organizations/"
                        + organizationId
                        + "/users/"
                        + userId
                        + "/"
                        + UUID.randomUUID()
                        + "-"
                        + cleanFileName;

        try {

            PutObjectRequest putObjectRequest =
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(s3Key)
                            .contentType(file.getContentType())
                            .contentLength(file.getSize())
                            .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(
                            file.getInputStream(),
                            file.getSize()
                    )
            );

            // =================================================
            // GENERATE S3 URL
            // =================================================

            GetUrlRequest getUrlRequest =
                    GetUrlRequest.builder()
                            .bucket(bucketName)
                            .key(s3Key)
                            .build();

            return s3Client.utilities()
                    .getUrl(getUrlRequest)
                    .toExternalForm();

        } catch (S3Exception e) {

            throw new RuntimeException(
                    "Failed to upload file to S3: "
                            + e.awsErrorDetails()
                            .errorMessage(),
                    e
            );
        }
    }
}