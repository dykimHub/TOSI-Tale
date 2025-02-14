package com.tosi.tale.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tosi.common.dto.TaleCacheDto;
import lombok.*;

import java.util.List;


@With
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaleDto {

    private long taleId;
    private String title;
    private String thumbnailS3Key;
    private String thumbnailS3URL;
    private int ttsLength;

    @Builder
    public TaleDto(long taleId, String title, String thumbnailS3Key, String thumbnailS3URL, int ttsLength) {
        this.taleId = taleId;
        this.title = title;
        this.thumbnailS3Key = thumbnailS3Key;
        this.thumbnailS3URL = thumbnailS3URL;
        this.ttsLength = ttsLength;
    }

    /*
     * Tale 엔티티에서 필요한 행만 매핑할 객체(contentS3Key, imagesS3KeyPrefix 미포함)
     */
    @QueryProjection
    public TaleDto(long taleId, String title, String thumbnailS3Key, int ttsLength) {
        this.taleId = taleId;
        this.title = title;
        this.thumbnailS3Key = thumbnailS3Key;
        this.ttsLength = ttsLength;
    }

    /**
     * TaleDto를 TaleCacheDto로 변환하여 반환합니다.
     *
     * @return 변환된 TaleCacheDto 객체
     */
    public TaleCacheDto toTaleCacheDto() {
        return TaleCacheDto.builder()
                .taleId(this.getTaleId())
                .title(this.getTitle())
                .thumbnailS3URL(this.getThumbnailS3URL())
                .ttsLength(this.getTtsLength())
                .build();
    }
}

