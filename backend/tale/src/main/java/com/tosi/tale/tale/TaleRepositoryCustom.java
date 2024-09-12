package com.tosi.tale.tale;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaleRepositoryCustom {

    Optional<List<TaleDto>> findTaleList(Pageable pageable);

    Optional<TaleDetailS3Dto> findTale(Long taleId);
}
