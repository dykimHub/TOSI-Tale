package com.tosi.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;


@JsonInclude(JsonInclude.Include.NON_NULL) // TaleBaseDto에 포함된 변수 중 null값 제거하고 캐싱
@RedisHash("TaleDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaleCacheDto extends TaleBaseDto {
    private String thumbnailS3URL;

    @Builder
    public TaleCacheDto(long taleId, String title, String thumbnailS3URL, int ttsLength) {
        super(taleId, title, null, null, null, ttsLength);
        this.thumbnailS3URL = thumbnailS3URL;
    }

}

