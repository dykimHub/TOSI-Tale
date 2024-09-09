package com.tosi.tale.tale;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaleDto {

    private int taleId;
    private String title;
    private String thumbnailS3URL;
    private int time;

    @QueryProjection
    @Builder
    public TaleDto(int taleId, String title, String thumbnailS3URL, int time) {
        this.taleId = taleId;
        this.title = title;
        this.thumbnailS3URL = thumbnailS3URL;
        this.time = time;
    }



}
