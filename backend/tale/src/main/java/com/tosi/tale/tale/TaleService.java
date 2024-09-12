package com.tosi.tale.tale;


import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaleService {

    List<TaleDto> findTaleList(Pageable pageable);

    TaleDetailDto findTale(Long taleId);
}
