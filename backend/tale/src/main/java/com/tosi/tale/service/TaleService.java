package com.tosi.tale.service;


import com.tosi.tale.dto.TaleDetailDto;
import com.tosi.tale.dto.TaleDto;
import com.tosi.tale.dto.TalePageRequestDto;
import com.tosi.tale.dto.TalePageResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaleService {

    TaleDto.TaleDtos findTaleList(Pageable pageable);

    TaleDto findTale(Long taleId);

    TaleDetailDto findTaleDetail(Long taleId);

    List<TaleDto> findTaleByTitle(String titlePart, Pageable pageable);

    List<TalePageResponseDto> createTalePages(TalePageRequestDto talePageRequestDto);

}
