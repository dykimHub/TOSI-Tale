package com.tosi.tale.dto;

import com.tosi.common.cache.TaleDetailCacheDto;
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

    /**
     * 정적 팩토리 메서드 of를 사용하여 새로운 TaleDetailDto를 생성합니다.
     * static 메서드이므로 인스턴스를 만들지 않고 직접 호출 가능합니다.
     */
    public static TaleDetailDto of(long taleId, String title, String content, String[] characters, List<String> images) {
        return TaleDetailDto.builder()
                .taleId(taleId)
                .title(title)
                .content(content)
                .characters(characters)
                .images(images)
                .build();
    }

    /**
     * 현재 객체를 기반으로 TaleDetailCacheDto를 생성합니다.
     */
    public TaleDetailCacheDto toTaleDetailCacheDto() {
        return TaleDetailCacheDto.builder()
                .taleId(this.taleId)
                .title(this.title)
                .content(this.content)
                .characters(this.characters)
                .images(this.images)
                .build();
    }
}
