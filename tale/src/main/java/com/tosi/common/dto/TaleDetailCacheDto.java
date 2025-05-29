package com.tosi.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL) // TaleBaseDto에 포함된 변수 중 null값 제거하고 캐싱
@RedisHash("TaleDetailDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaleDetailCacheDto extends TaleBaseDto {
    private String content;
    private String[] characters;
    private List<String> images;

    /**
     * TaleDetailDto를 서비스 레이어에서 가공한 후 최종 반환하는 객체
     * contentS3Key -> contents, characters / imagesS3KeyPrefix -> images
     */
    @Builder
    public TaleDetailCacheDto(Long taleId, String title, String content, String[] characters, List<String> images) {
        super(taleId, title, null, null, null, 0);
        this.content = content;
        this.characters = characters;
        this.images = images;
    }

}