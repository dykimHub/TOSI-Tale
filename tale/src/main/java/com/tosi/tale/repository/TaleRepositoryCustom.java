package com.tosi.tale.repository;

import com.tosi.tale.dto.TaleDetailDto;
import com.tosi.tale.dto.TaleDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaleRepositoryCustom {

    List<Long> findTaleIdList(Pageable pageable);

    Optional<TaleDto> findTale(Long taleId);

    List<TaleDto> findMultiTales(List<Long> taleIds);

    Optional<TaleDetailDto> findTaleDetail(Long taleId);

    List<TaleDetailDto> findMultiTaleDetails(List<Long> taleIds);

    List<Long> findTaleByTitle(String titlePart, Pageable pageable);

}
