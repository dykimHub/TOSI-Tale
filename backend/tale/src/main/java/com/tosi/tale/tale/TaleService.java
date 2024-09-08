package com.tosi.tale.tale;


import com.ssafy.tosi.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
//final field에 대한 생성자 주입
public class TaleService {
    private final TaleRepository taleRepository;
    private final S3Service s3Service;

    public List<TaleDto> selectAllTales() {
        List<Tale> tales = taleRepository.findAll();

        List<TaleDto> taleDtos = new ArrayList<>();
        for (Tale tale : tales) {
            String[] images = tale.getImages().split(",");
            for (int i = 0; i < images.length; i++)
                images[i] = s3Service.getPath(images[i]);

            String total_contents = "";
            total_contents += tale.getContent1();
            total_contents += tale.getContent2();
            total_contents += tale.getContent3();
            if (tale.getContent4() != null)
                total_contents += tale.getContent4();

            String[] characters = tale.getCharacters().split(",");

            TaleDto taleDto = TaleDto.builder()
                    .taleId(tale.getTaleId())
                    .title(tale.getTitle())
                    .content1(tale.getContent1())
                    .content2(tale.getContent2())
                    .content3(tale.getContent3())
                    .content4(tale.getContent4())
                    .total_contents(total_contents)
                    .images(images)
                    .thumbnail(images[0])
                    .characters(characters)
                    .time(tale.getTime())
                    .likeCnt(tale.getLikeCnt())
                    .build();

            taleDtos.add(taleDto);

        }


        return taleDtos;
    }

    public Tale selectOneTale(int taleId) {
        return taleRepository.findById(taleId).get();
    }

    public List<TaleDto> selectByTitle(String title) {
        List<Tale> tales = taleRepository.findByTitleContaining(title);

        List<TaleDto> taleDtos = new ArrayList<>();
        for (Tale tale : tales) {
            String[] images = tale.getImages().split(",");
            for (int i = 0; i < images.length; i++)
                images[i] = s3Service.getPath(images[i]);

            String total_contents = "";
            total_contents += tale.getContent1();
            total_contents += tale.getContent2();
            total_contents += tale.getContent3();
            if (tale.getContent4() != null)
                total_contents += tale.getContent4();

            String[] characters = tale.getCharacters().split(",");

            TaleDto taleDto = TaleDto.builder()
                    .taleId(tale.getTaleId())
                    .title(tale.getTitle())
                    .content1(tale.getContent1())
                    .content2(tale.getContent2())
                    .content3(tale.getContent3())
                    .content4(tale.getContent4())
                    .total_contents(total_contents)
                    .images(images)
                    .thumbnail(images[0])
                    .characters(characters)
                    .time(tale.getTime())
                    .likeCnt(tale.getLikeCnt())
                    .build();

            taleDtos.add(taleDto);

        }


        return taleDtos;
    }
}
