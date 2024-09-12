package com.tosi.tale.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/s3")
@RequiredArgsConstructor
@RestController
public class S3Controller {
//    private final S3Service s3UploadService;
//
//    @GetMapping("/tales/s3/download/{fileName}")
//    public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
//        try {
//            String filePath = s3UploadService.getPath(fileName);
//            return new ResponseEntity<String>(filePath, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }

//    @PostMapping("/tales/s3/upload")
//    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("fileName") String fileName) {
//        try {
//            String uploadUrl = s3UploadService.uploadFile(file, fileName);
//            return new ResponseEntity<String>(uploadUrl, HttpStatus.OK);
//        } catch (IOException e) {
//            return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
//        } catch (Exception e) {
//            return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @PostMapping("/tales/s3/upload")
//    public String uploadImageToS3(@RequestBody String imageUrl) {
//        // 이미지 다운로드 및 S3 업로드 로직
//        return s3UploadService.uploadImageFromUrl(imageUrl);
//        }

}
