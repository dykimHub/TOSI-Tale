package com.tosi.tale.tale;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TaleDto {

    private int taleId;
    private String title;
    private String content1;
    private String content2;
    private String content3;
    private String content4;
    private String total_contents;
    private String[] images;
    private String thumbnail;
    private String[] characters;
    private int time;
    private int likeCnt;

}
