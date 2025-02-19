package com.tosi.tale.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tosi.common.dto.TaleCacheDto;
import com.tosi.common.dto.TaleDto;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaleDtoImpl extends TaleDto {
    private String thumbnailS3URL;

    /*
     * Tale 엔티티에서 필요한 행만 매핑할 객체(contentS3Key, imagesS3KeyPrefix 미포함)
     */
    @QueryProjection
    public TaleDtoImpl(long taleId, String title, String thumbnailS3Key, int ttsLength) {
        super(taleId, title, thumbnailS3Key, ttsLength);
    }

    /**
     * TaleDto를 TaleCacheDto로 변환하여 반환합니다.
     * S3 Key를 제외하여 객체 크기를 줄입니다.
     *
     * @return 변환된 TaleCacheDto 객체
     */
    public TaleCacheDto withS3URL(String thumbnailS3URL) {
        return TaleCacheDto.builder()
                .taleId(this.getTaleId())
                .title(this.getTitle())
                .thumbnailS3URL(thumbnailS3URL)
                .ttsLength(this.getTtsLength())
                .build();
    }
}

