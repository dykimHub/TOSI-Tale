package com.tosi.tale.tale;


import org.springframework.data.domain.Pageable;

public interface TaleService {

    TaleDto.TaleDtos findTaleList(Pageable pageable);

    TaleDetailDto findTale(Long taleId);
}
