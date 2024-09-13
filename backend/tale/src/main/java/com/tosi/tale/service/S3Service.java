package com.tosi.tale.service;

import com.amazonaws.services.s3.model.S3Object;

import java.util.List;

public interface S3Service {

    S3Object findS3Object(String s3Key);

    String findS3URL(String s3key);

    String findContents(String s3Key);

    String[] findCharacters(String s3Key);

    List<String> findImages(String s3keyPrefix);
}
