package com.tosi.tale.tale;


import java.util.List;

public interface TaleService {

    List<TaleDto> findTaleList();

    TaleDetailDto findTale(Long taleId);
}
