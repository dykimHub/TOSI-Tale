package com.tosi.tale.dto;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;

@Getter
public class TalePageRequestDto {
    private long taleId;
    private String content;
    private String[] characters;
    private List<String> images;

    private LinkedHashMap<String, String> nameMap;
}
