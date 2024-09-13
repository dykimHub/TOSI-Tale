package com.tosi.tale.repository;

import com.tosi.tale.dto.TaleDetailS3Dto;
import com.tosi.tale.dto.TaleDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaleRepositoryCustom {

    Optional<List<TaleDto>> findTaleList(Pageable pageable);

    Optional<TaleDetailS3Dto> findTale(Long taleId);

    List<TaleDto> findTaleByTitle(String titlePart, Pageable pageable);
}
