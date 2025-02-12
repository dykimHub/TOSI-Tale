package com.tosi.tale.service;


import com.tosi.common.cache.TaleCacheDto;
import com.tosi.common.cache.TaleDetailCacheDto;
import com.tosi.tale.dto.TaleDetailDto;
import com.tosi.tale.dto.TaleDto;
import com.tosi.tale.dto.TalePageRequestDto;
import com.tosi.tale.dto.TalePageResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaleService {

    TaleDto.TaleDtos findTaleList(Pageable pageable);

    TaleCacheDto findTale(Long taleId);

    List<TaleCacheDto> findMultiTales(List<Long> taleIds);

    TaleDetailDto findTaleDetail(Long taleId);

    List<TaleDetailCacheDto> findMultiTaledetails(List<Long> taleIds);

    List<TaleDto> findTaleByTitle(String titlePart, Pageable pageable);

    List<TalePageResponseDto> createTalePages(TalePageRequestDto talePageRequestDto);

    Long findUserAuthorization(String accessToken);

}
