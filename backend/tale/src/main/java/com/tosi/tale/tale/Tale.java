package com.tosi.tale.tale;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="tales")
public class Tale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taleId;

    private String title;
    private String content1;
    private String content2;
    private String content3;
    private String content4;
    private String images;
    private String characters;
    private int time;
    private int likeCnt;
}