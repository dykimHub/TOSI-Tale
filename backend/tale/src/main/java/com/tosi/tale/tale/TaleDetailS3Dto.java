package com.tosi.tale.tale;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaleDetailS3Dto {

    private long taleId;
    private String title;
    private String contentS3Key;
    private String imageS3KeyPrefix;

    /*
     * Tale 엔티티에서 필요한 행만 매핑할 객체(thumbnailS3Key 미포함)
     */
    @QueryProjection
    public TaleDetailS3Dto(long taleId, String title, String contentS3Key, String imageS3KeyPrefix){
        this.taleId = taleId;
        this.title = title;
        this.contentS3Key = contentS3Key;
        this.imageS3KeyPrefix = imageS3KeyPrefix;
    }
}
