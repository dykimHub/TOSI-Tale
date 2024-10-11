package com.tosi.tale.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.LinkedHashMap;

@Getter
public class TalePageRequestDto {
    private TaleDetailDto taleDetailDto;
    private LinkedHashMap<String, String> nameMap;
}
