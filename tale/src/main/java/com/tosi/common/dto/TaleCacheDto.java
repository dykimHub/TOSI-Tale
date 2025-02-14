package com.tosi.common.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("TaleDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaleCacheDto {

    @Id // Redis 고유 식별자
    private long taleId;
    private String title;
    private String thumbnailS3URL;
    private int ttsLength;

    /**
     * TaleDto에서 S3 Key 변수를 제외해서 Redis에 저장될 객체 크기를 줄임
     */
    @Builder
    public TaleCacheDto(long taleId, String title, String thumbnailS3URL, int ttsLength) {
        this.taleId = taleId;
        this.title = title;
        this.thumbnailS3URL = thumbnailS3URL;
        this.ttsLength = ttsLength;
    }

}

