package com.tosi.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class TaleBaseDto {
    @Id // 레디스 고유 식별자; 자식 클래스(TaleCacheDto)에서 중복 선언하면 키 충돌해서 부모 클래스에 정의
    private long taleId;
    private String title;
    private String thumbnailS3Key;
    private int ttsLength;

    // protected; 패키지가 다를 경우 상속된 클래스에서만 생성자 호출 가능
    protected TaleBaseDto(long taleId, String title, String thumbnailS3Key, int ttsLength) {
        this.taleId = taleId;
        this.title = title;
        this.thumbnailS3Key = thumbnailS3Key;
        this.ttsLength = ttsLength;
    }

    public TaleBaseDto(long taleId, String title, int ttsLength) {
        this.taleId = taleId;
        this.title = title;
        this.ttsLength = ttsLength;
    }

    // 자식 클래스 구현
    public abstract TaleCacheDto toTaleCacheDto(String s3URL);

}
