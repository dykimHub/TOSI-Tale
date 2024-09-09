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

}
