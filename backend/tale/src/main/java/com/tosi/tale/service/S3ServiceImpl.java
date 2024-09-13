package com.tosi.tale.service;

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
        return amazonS3.getUrl(bucketName, s3key).toString();
    }

    /**
     * AWS S3에서 해당 S3 Key의 파일(동화 본문)을 읽고 구간별로 반환합니다.
     *
     * @param s3Key S3 객체의 Key
     * @return 구간별로 나뉜 동화 본문 배열(구분자: "-----")
     * @throws CustomException 파일을 읽을 수 없는 경우 예외 처리
     */
    @Override
    public String findContents(String s3Key) {
        // try-with-resoures; 텍스트 파일을 버퍼로 읽고 자동으로 스트림을 닫음
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                findS3Object(s3Key).getObjectContent()))) {

            StringBuilder sb = new StringBuilder();
            String line;
            // 파일 끝까지 한 줄씩 읽고 StringBuilder에 추가
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            return sb.toString();
        } catch (IOException e) {
            throw new CustomException(ExceptionCode.TALE_CONTENT_FETCH_FAIL);
        }
    }

    /**
     * AWS S3에서 해당 S3 Key의 메타데이터(등장인물)를 반환합니다.
     *
     * @param s3Key S3 객체의 Key
     * @return 디코딩된 등장인물 배열(구분자: 콤마)
     * @throws CustomException 메타데이터를 가져올 수 없는 경우 예외 처리
     */
    @Override
    public String[] findCharacters(String s3Key) {
        String encodedCharacters = findS3Object(s3Key).getObjectMetadata().getUserMetadata().get("characters");
        if (encodedCharacters == null) {
            throw new CustomException(ExceptionCode.TALE_CHARACTERS_FETCH_FAIL);
        }
        return URLDecoder.decode(encodedCharacters, StandardCharsets.UTF_8).split(",");

    }

    /**
     * AWS S3에서 해당 S3 Key의 Prefix로 동화별 폴더 내 삽화 파일의 주소 목록을 반환합니다.
     *
     * @param s3keyPrefix S3 객체의 Key Prefix
     * @return 삽화 파일 주소(S3 객체 URL) 리스트
     * @throws CustomException 폴더를 가져올 수 없는 경우 예외 처리
     */
    @Override
    public List<String> findImages(String s3keyPrefix) {
        // S3 버킷에서 주어진 prefix로 객체 목록을 조회하는 요청을 생성
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(s3keyPrefix)
                .build();

        ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);
        if (listObjectsV2Response == null) {
            throw new CustomException(ExceptionCode.TALE_IMAGES_FETCH_FAIL);
        }

        // S3 객체 리스트에서 각 객체의 Key로 S3 객체 URL를 생성한 후 추가
        return listObjectsV2Response.contents().stream()
                .map(s3Object -> findS3URL(s3Object.key()))
                .toList();
    }


}