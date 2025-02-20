package com.tosi.tale.repository;

import com.tosi.tale.dto.TaleDetailS3Dto;
import com.tosi.tale.dto.TaleDtoImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaleRepositoryCustom {

    List<Long> findTaleIdList(Pageable pageable);

    Optional<TaleDtoImpl> findTale(Long taleId);

    List<TaleDtoImpl> findMultiTales(List<Long> taleIds);

    Optional<TaleDetailS3Dto> findTaleDetail(Long taleId);

    List<TaleDetailS3Dto> findMultiTaleDetails(List<Long> taleIds);

    List<Long> findTaleByTitle(String titlePart, Pageable pageable);

}
