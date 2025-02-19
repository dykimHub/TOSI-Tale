package com.tosi.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@JsonInclude(JsonInclude.Include.NON_NULL)
@RedisHash("TaleDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaleCacheDto extends TaleDto {

    @Id // Redis 고유 식별자
    private long taleId;
    private String thumbnailS3URL;

    @Builder
    public TaleCacheDto(long taleId, String title, String thumbnailS3URL, int ttsLength) {
        super(taleId, title, ttsLength);
        this.taleId = taleId;
        this.thumbnailS3URL = thumbnailS3URL;
    }

}

