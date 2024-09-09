package com.tosi.tale.tale;

import com.amazonaws.services.s3.model.S3Object;
import com.tosi.tale.common.exception.CustomException;
import com.tosi.tale.common.exception.ExceptionCode;
import com.tosi.tale.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaleServiceImpl implements TaleService {
    private final TaleRepository taleRepository;
    private final S3Service s3Service;

    /**
     * 동화 목록을 TaleDto 객체 리스트로 반환합니다.
     *
     * @return TaleDto 객체 리스트
     * @throws CustomException 동화 목록이 없을 경우 예외 처리
     */
    @Override
    public List<TaleDto> findTaleList() {
        List<TaleDto> taleDtoList = taleRepository.findTaleList()
                .orElseThrow(() -> new CustomException(ExceptionCode.ALL_TALES_NOT_FOUND));

        return taleDtoList.stream()
                .map(taleDto -> taleDto.withThumbnailS3URL(
                        s3Service.findS3URL(taleDto.getThumbnailS3Key()))
                )
                .toList();
    }

    /**
     * 동화 본문, 등장인물, 삽화 등을 포함한 상세 정보를 TaleDetailDto 객체로 반환합니다.
     *
     * @param taleId Tale 객체 id
     * @return TaleDetailDto 객체
     * @throws CustomException 해당 id의 동화가 없을 경우 예외 처리
     */
    @Override
    public TaleDetailDto findTale(Long taleId) {
        TaleDetailS3Dto taleDetailS3Dto = taleRepository.findTale(taleId)
                .orElseThrow(() -> new CustomException(ExceptionCode.TALE_NOT_FOUND));

        String[] contents = s3Service.findContents(taleDetailS3Dto.getContentS3Key());
        String[] characters = s3Service.findCharacters(taleDetailS3Dto.getContentS3Key());
        List<String> images = s3Service.findImages(taleDetailS3Dto.getImageS3KeyPrefix());

        return TaleDetailDto.builder()
                .taleId(taleDetailS3Dto.getTaleId())
                .title(taleDetailS3Dto.getTitle())
                .time(taleDetailS3Dto.getTime())
                .contents(contents)
                .characters(characters)
                .images(images)
                .build();
    }

}
