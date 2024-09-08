package com.tosi.tale.taleDetail;

import com.ssafy.tosi.tale.TaleDto;
import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;
import java.util.List;

public interface TaleDetailService {
    TaleDto getTaleDetail(int taleId) throws EntityNotFoundException;
    void selectName(String CName, String BName);
    String[] split_sentences(TaleDto taleDto) throws IOException;
    String[] changeName(String[] contents) throws Exception;
    List<Page> paging(String[] changedContents, TaleDto taleDto);
    int updateLikeCnt(int taleId);
}
