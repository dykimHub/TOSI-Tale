package com.tosi.tale.taleDetail;

//import com.ssafy.tosi.tale.TaleDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class TaleDetailController {

    private final TaleDetailService taleDetailService;

//    @GetMapping("/tales/{taleId}")
//    public ResponseEntity<?> getTaleDetail(HttpServletRequest request, @PathVariable int taleId) {
//        try {
//            TaleDto taleDto = taleDetailService.getTaleDetail(taleId);
//            return new ResponseEntity<TaleDto>(taleDto, HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            String errMsg = "No Data";
//            return new ResponseEntity<String>(errMsg, HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
//
//    @PostMapping("/tales/{CName}/{BName}")
//    public ResponseEntity<?> selectName(HttpServletRequest request, @PathVariable String CName, @PathVariable String BName) {
//        try {
//            taleDetailService.selectName(CName, BName);
//            return new ResponseEntity<Void>(HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
//
//    @PostMapping("/tales/read")
//    public ResponseEntity<?> readBook(HttpServletRequest request, @RequestBody TaleDto taleDto) {
//        try {
//            String[] contents = {taleDto.getContent1(), taleDto.getContent2(), taleDto.getContent3(), taleDto.getContent4()};
//            String[] changedContents = taleDetailService.changeName(contents); // 이름 바꾸기
//            List<Page> pages = taleDetailService.paging(changedContents, taleDto); // 페이지 형식으로 변경
//            return new ResponseEntity<List<Page>>(pages, HttpStatus.OK);
//        } catch (IOException e) {
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
//
//    @GetMapping("/tales/like/{taleId}")
//    public ResponseEntity<?> getLikeCnt(@PathVariable int taleId) {
//        try {
//            int likeCnt = taleDetailService.updateLikeCnt(taleId);
//            return new ResponseEntity<Integer>(likeCnt, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }


}
