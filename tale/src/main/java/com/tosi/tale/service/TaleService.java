package com.tosi.tale.service;


import com.tosi.common.dto.TaleCacheDto;
import com.tosi.common.dto.TaleDetailCacheDto;
import com.tosi.common.dto.TalePageDto;
import com.tosi.tale.dto.TaleDetailDto;
import com.tosi.tale.dto.TalePageRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaleService {

    List<TaleCacheDto> findTaleList(Pageable pageable);

    List<TaleCacheDto> findMultiTales(List<Long> taleIds);

    List<TaleCacheDto> findTaleByTitle(String titlePart, Pageable pageable);

    TaleDetailDto findTaleDetail(Long taleId);

    List<TaleDetailCacheDto> findMultiTaleDetails(List<Long> taleIds);

    List<TalePageDto> createTalePages(TalePageRequestDto talePageRequestDto);

    Long findUserAuthorization(String accessToken);

}
