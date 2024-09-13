package com.tosi.tale.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TalePageResponseDto {
    private int leftNo;
    private String left;
    private int rightNo;
    private String right;
    private boolean flipped;

    @Builder
    public TalePageResponseDto(int leftNo, String left, int rightNo, String right, boolean flipped) {
        this.leftNo = leftNo;
        this.left = left;
        this.rightNo = rightNo;
        this.right = right;
        this.flipped = flipped;
    }
}