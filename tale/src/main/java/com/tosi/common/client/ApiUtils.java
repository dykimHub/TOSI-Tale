package com.tosi.common.client;

import com.tosi.common.dto.TalePageDto;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass // 메서드 static 변환, 인스턴스 형성 방지
public class ApiUtils {

    /**
     * 동화 페이지를 생성합니다.
     * 왼쪽 페이지는 삽화, 오른쪽 페이지는 동화 본문을 2문장씩 삽입합니다.
     *
     * @param contents 동화 내용 배열
     * @param images   이미지 주소 리스트(DalleURL, S3URL)
     * @return TalePageDto 리스트
     */
    public List<TalePageDto> createTalePages(String[] contents, List<String> images) {
        List<TalePageDto> talePages = new ArrayList<>();
        int page = 1;

        for (int i = 0; i < contents.length; i++) {
            String content = contents[i]; // 동화 본문
            String imageURL = images.get(i); // 삽화

            String[] lines = content.split("\n");
            for (int j = 0; j < lines.length; j += 2) {
                String line1 = lines[j];
                // line1이 마지막 문장이면 다음 문장은 빈 문장
                String line2 = (j + 1 < lines.length) ? lines[j + 1] : "";

                talePages.add(
                        TalePageDto.builder()
                                .leftNo(page++)
                                .left(imageURL)
                                .rightNo(page++)
                                .right(line1 + "\n" + line2)
                                .build()
                );

            }


        }

        return talePages;
    }
}
