package com.tosi.tale.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tosi.common.dto.TaleBaseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaleDetailDto extends TaleBaseDto {
    /*
     * Tale 엔티티에서 필요한 행만 매핑할 객체(thumbnailS3Key 미포함)
     */
    @QueryProjection
    public TaleDetailDto(Long taleId, String title, String contentS3Key, String imageS3KeyPrefix) {
        super(taleId, title, null, contentS3Key, imageS3KeyPrefix, 0);
    }

}
