package com.tosi.tale.tale;

import com.tosi.tale.common.exception.CustomException;
import com.tosi.tale.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaleServiceImpl implements TaleService {
    private final TaleRepository taleRepository;

    /**
     * 동화 목록을 TaleDto 객체 리스트로 반환합니다.
     *
     * @return TaleDto 객체 리스트
     * @throws CustomException 동화 목록이 없을 경우 예외 처리
     */
    @Override
    public List<TaleDto> findTaleList() {
        return taleRepository.findTaleList()
                .orElseThrow(() -> new CustomException(ExceptionCode.ALL_TALES_NOT_FOUND));
    }
//    private final S3Service s3Service;
//
//    public List<TaleDto> selectAllTales() {
//        List<Tale> tales = taleRepository.findAll();
//
//        List<TaleDto> taleDtos = new ArrayList<>();
//        for (Tale tale : tales) {
//            String[] images = tale.getImages().split(",");
//            for (int i = 0; i < images.length; i++)
//                images[i] = s3Service.getPath(images[i]);
//
//            String total_contents = "";
//            total_contents += tale.getContent1();
//            total_contents += tale.getContent2();
//            total_contents += tale.getContent3();
//            if (tale.getContent4() != null)
//                total_contents += tale.getContent4();
//
//            String[] characters = tale.getCharacters().split(",");
//
//            TaleDto taleDto = TaleDto.builder()
//                    .taleId(tale.getTaleId())
//                    .title(tale.getTitle())
//                    .content1(tale.getContent1())
//                    .content2(tale.getContent2())
//                    .content3(tale.getContent3())
//                    .content4(tale.getContent4())
//                    .total_contents(total_contents)
//                    .images(images)
//                    .thumbnail(images[0])
//                    .characters(characters)
//                    .time(tale.getTime())
//                    .likeCnt(tale.getLikeCnt())
//                    .build();
//
//            taleDtos.add(taleDto);
//
//        }
//
//
//        return taleDtos;
//    }
//
//    public Tale selectOneTale(int taleId) {
//        return taleRepository.findById(taleId).get();
//    }
//
//    public List<TaleDto> selectByTitle(String title) {
//        List<Tale> tales = taleRepository.findByTitleContaining(title);
//
//        List<TaleDto> taleDtos = new ArrayList<>();
//        for (Tale tale : tales) {
//            String[] images = tale.getImages().split(",");
//            for (int i = 0; i < images.length; i++)
//                images[i] = s3Service.getPath(images[i]);
//
//            String total_contents = "";
//            total_contents += tale.getContent1();
//            total_contents += tale.getContent2();
//            total_contents += tale.getContent3();
//            if (tale.getContent4() != null)
//                total_contents += tale.getContent4();
//
//            String[] characters = tale.getCharacters().split(",");
//
//            TaleDto taleDto = TaleDto.builder()
//                    .taleId(tale.getTaleId())
//                    .title(tale.getTitle())
//                    .content1(tale.getContent1())
//                    .content2(tale.getContent2())
//                    .content3(tale.getContent3())
//                    .content4(tale.getContent4())
//                    .total_contents(total_contents)
//                    .images(images)
//                    .thumbnail(images[0])
//                    .characters(characters)
//                    .time(tale.getTime())
//                    .likeCnt(tale.getLikeCnt())
//                    .build();
//
//            taleDtos.add(taleDto);
//
//        }
//
//
//        return taleDtos;
//    }
}
