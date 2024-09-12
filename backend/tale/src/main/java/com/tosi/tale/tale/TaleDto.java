package com.tosi.tale.tale;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@ToString
@With
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaleDto {

    private long taleId;
    private String title;
    private String thumbnailS3Key;
    private String thumbnailS3URL;
    private int ttsLength;

    /*
     * 서비스 레이어에서 thumbnailS3URL을 추가한 후 사용하는 생성자
     */
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

    /*
     * Wrapper Class; TaleDto 객체 리스트를 하나의 객체로 감싸는 클래스
     * Redis에 TaleDto 객체 리스트를 저장할 때 단일 객체로 감싸서 직렬화/역직렬화 과정에서 명확한 구조 유지
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TaleDtos {
        private List<TaleDto> taleDtos;

        public TaleDtos(List<TaleDto> taleDtos) {
            this.taleDtos = taleDtos;
        }
    }
}

