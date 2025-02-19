package com.tosi.common.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class TaleDto {
    private long taleId;
    private String title;
    private String thumbnailS3Key;
    private int ttsLength;

    // protected; 패키지가 다를 경우 상속된 클래스에서만 생성자 호출 가능
    protected TaleDto(long taleId, String title, int ttsLength) {
        this.taleId = taleId;
        this.title = title;
        this.ttsLength = ttsLength;
    }

    protected TaleDto(long taleId, String title, String thumbnailS3Key, int ttsLength) {
        this.taleId = taleId;
        this.title = title;
        this.thumbnailS3Key = thumbnailS3Key;
        this.ttsLength = ttsLength;
    }
}
