package com.tosi.tale.service;


import com.tosi.common.dto.TaleCacheDto;
import com.tosi.common.dto.TaleDetailCacheDto;
import com.tosi.tale.dto.TaleDetailDto;
import com.tosi.tale.dto.TaleDto;
import com.tosi.tale.dto.TalePageRequestDto;
import com.tosi.tale.dto.TalePageResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaleService {

    List<TaleDto> findTaleList(Pageable pageable);

    TaleCacheDto findTale(Long taleId);

    List<TaleCacheDto> findMultiTales(List<Long> taleIds);

    TaleDetailDto findTaleDetail(Long taleId);

    List<TaleDetailCacheDto> findMultiTaleDetails(List<Long> taleIds);

    List<TaleDto> findTaleByTitle(String titlePart, Pageable pageable);

    List<TalePageResponseDto> createTalePages(TalePageRequestDto talePageRequestDto);

    Long findUserAuthorization(String accessToken);

}
