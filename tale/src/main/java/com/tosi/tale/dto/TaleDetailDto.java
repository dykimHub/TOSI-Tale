package com.tosi.tale.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaleDetailDto {

    private long taleId;
    private String title;
    private String content;
    private String[] characters;
    private List<String> images;

    /**
     * TaleDetailS3Dto를 서비스 레이어에서 가공한 후 최종 반환하는 객체
     * contentS3Key -> contents, characters / imagesS3KeyPrefix -> images
     */
    @Builder
    public TaleDetailDto(long taleId, String title, String content, String[] characters, List<String> images) {
        this.taleId = taleId;
        this.title = title;
        this.content = content;
        this.characters = characters;
        this.images = images;
    }
}
