package com.tosi.tale.tale;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tales")
public class Tale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taleId;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "content_s3_url", nullable = false)
    private String contentS3URL;
    @Column(name = "thumbnail_s3_url", nullable = false)
    private String thumbnailS3URL;
    @Column(name = "images_s3_url", nullable = false)
    private String imagesS3URL;
    @Column(name = "time", nullable = false)
    private int time;

}