package com.tosi.tale.tale;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tales")
public class Tale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long taleId;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "content_s3_key", nullable = false)
    private String contentS3Key;
    @Column(name = "thumbnail_s3_key", nullable = false)
    private String thumbnailS3Key;
    @Column(name = "images_s3_key_prefix", nullable = false)
    private String imagesS3KeyPrefix;
    @Column(name = "tts_length", nullable = false)
    private int ttsLength;
    @CreationTimestamp
    @Column(name = "reg_date", nullable = false)
    private OffsetDateTime regDate;


}