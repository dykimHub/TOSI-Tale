package com.tosi.tale.s3;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;
    private final S3Client s3Client;

    private String bucketName = "talebucket";

    public String getPath(String fileName) {
        return amazonS3.getUrl(bucketName, fileName).toString();
    }
//
//    public String uploadFile(MultipartFile multipartFile, String fileName) throws IOException {
//        File file = convertMultiPartFileToFile(multipartFile);
//        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file));
//        file.delete();
//        return amazonS3.getUrl(bucketName, fileName).toString();
//    }
//
//    private File convertMultiPartFileToFile(MultipartFile multipartFile) throws IOException {
//        File file = new File(multipartFile.getOriginalFilename());
//        try (FileOutputStream fos = new FileOutputStream(file)) {
//            fos.write(multipartFile.getBytes());
//        }
//        return file;
//    }

    public String uploadImageFromUrl(String imageUrl) {
        String fileName = convertUrlToFileName(imageUrl);
        try {
            URL url = new URL(imageUrl);

            System.setProperty("java.net.useSystemProxies", "false");
            InputStream inputStream = url.openStream();
            byte[] imageBytes = inputStream.readAllBytes();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType("image/png")
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageBytes));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException Details: " + e.getMessage());
        }
        return fileName;
    }
    private String convertUrlToFileName(String imageUrl) {
        String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
        fileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_"); // 영문 대소문자, 숫자, 점(.)을 제외한 문자를 밑줄(_)로 대체
        return fileName;}
}