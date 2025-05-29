package com.tosi.tale.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tosi.common.dto.TaleBaseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaleDto extends TaleBaseDto {
    /*
     * Tale 엔티티에서 필요한 행만 매핑할 객체(contentS3Key, imagesS3KeyPrefix 등 미포함)
     */
    @QueryProjection
    public TaleDto(Long taleId, String title, String thumbnailS3Key, int ttsLength) {
        super(taleId, title, thumbnailS3Key, null, null, ttsLength);
    }

}

