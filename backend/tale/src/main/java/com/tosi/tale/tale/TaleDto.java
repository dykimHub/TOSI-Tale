package com.tosi.tale.tale;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;


@ToString
@With
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaleDto {

    private long taleId;
    private String title;
    private String thumbnailS3Key;
    private String thumbnailS3URL;
    private int time;

    /**
     * 서비스 레이어에서 thumbnailS3URL을 추가한 후 사용하는 생성자
     */
    @Builder
    public TaleDto(long taleId, String title, String thumbnailS3Key, String thumbnailS3URL, int time) {
        this.taleId = taleId;
        this.title = title;
        this.thumbnailS3Key = thumbnailS3Key;
        this.thumbnailS3URL = thumbnailS3URL;
        this.time = time;
    }

    /**
     * Tale 엔티티에서 필요한 행만 매핑할 객체(contentS3Key, imagesS3KeyPrefix 미포함)
     */
    @QueryProjection
    public TaleDto(long taleId, String title, String thumbnailS3Key, int time) {
        this.taleId = taleId;
        this.title = title;
        this.thumbnailS3Key = thumbnailS3Key;
        this.time = time;
    }

}
