package com.tosi.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@NoArgsConstructor
public class TaleBaseDto {
    @Id // 레디스 고유 식별자; 자식 클래스에서 중복 선언하면 키 충돌해서 부모 클래스에 정의
    private Long taleId;
    private String title;
    private String thumbnailS3Key;
    private String contentS3Key;
    private String imageS3KeyPrefix;
    private int ttsLength;

    // protected; 패키지가 다를 경우 상속된 클래스에서만 생성자 호출 가능
    protected TaleBaseDto(Long taleId, String title, String thumbnailS3Key, String contentS3Key, String imageS3KeyPrefix, int ttsLength) {
        this.taleId = taleId;
        this.title = title;
        this.thumbnailS3Key = thumbnailS3Key;
        this.contentS3Key = contentS3Key;
        this.imageS3KeyPrefix = imageS3KeyPrefix;
        this.ttsLength = ttsLength;
    }
}
