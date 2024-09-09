package com.tosi.tale.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.tosi.tale.common.exception.CustomException;
import com.tosi.tale.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final AmazonS3 amazonS3;
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    /**
     * AWS S3에서 해당 S3 Key의 S3Object를 반환합니다.
     *
     * @param s3Key S3 객체의 Key
     * @return S3Object
     */
    @Override
    public S3Object findS3Object(String s3Key) {
        return amazonS3.getObject(new GetObjectRequest(bucketName, s3Key));
    }

    /**
     * AWS S3에서 해당 버킷 이름과 S3 Key로 S3 객체 URL을 생성합니다.
     *
     * @param s3key S3 객체의 Key
     * @return S3 객체의 URL
     */
    @Override
    public String findS3URL(String s3key) {
        findS3Object(s3key);
        return amazonS3.getUrl(bucketName, s3key).toString();
    }




}