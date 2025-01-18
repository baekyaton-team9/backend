package com.baekyaton.backend.global.external.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.baekyaton.backend.global.exception.ApiException;
import com.baekyaton.backend.global.exception.GlobalErrorCode;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImageFile(MultipartFile imageFile, String newFileName) {
        String originalName = imageFile.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf("."));
        String changedName = newFileName + ext;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + ext);
        metadata.setContentLength(imageFile.getSize());

        try {
            amazonS3.putObject(
                    new PutObjectRequest(bucket, changedName, imageFile.getInputStream(), metadata)
            );
        } catch (IOException e) {
            throw new ApiException(GlobalErrorCode.FILE_UPLOAD_FAILED, e);
        }

        return amazonS3.getUrl(bucket, changedName).toString();
    }
}
