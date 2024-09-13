package com.tosi.tale.tale;

import lombok.*;

import java.util.LinkedHashMap;

@ToString
@Getter
public class TalePageRequestDto {
    private TaleDetailDto taleDetailDto;
    private LinkedHashMap<String, String> nameMap;
}
