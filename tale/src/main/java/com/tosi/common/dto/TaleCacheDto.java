package com.tosi.common.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;


@RedisHash("TaleDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaleCacheDto extends TaleBaseDto {
    private String thumbnailS3URL;

    @Builder
    public TaleCacheDto(long taleId, String title, String thumbnailS3URL, int ttsLength) {
        super(taleId, title, ttsLength);
        this.thumbnailS3URL = thumbnailS3URL;
    }

    @Override
    public TaleCacheDto toTaleCacheDto(String s3URL) {
        return this;
    }
}

