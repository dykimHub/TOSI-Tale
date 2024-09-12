package com.tosi.tale.tale;


import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaleService {

    TaleDto.TaleDtos findTaleList(Pageable pageable);

    TaleDetailDto findTale(Long taleId);

    List<TaleDto> findTaleByTitle(String titlePart, Pageable pageable);
}
