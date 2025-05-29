package com.tosi.common.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TalePageDto {
    private int leftNo;
    private String left;
    private int rightNo;
    private String right;
    private boolean flipped;

    @Builder
    public TalePageDto(int leftNo, String left, int rightNo, String right, boolean flipped) {
        this.leftNo = leftNo;
        this.left = left;
        this.rightNo = rightNo;
        this.right = right;
        this.flipped = flipped;
    }
}