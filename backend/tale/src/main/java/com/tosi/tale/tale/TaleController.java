package com.tosi.tale.tale;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/api/tales")
@RequiredArgsConstructor
@RestController
public class TaleController {

    private final TaleService taleService;

    @Operation(summary = "동화 목록 조회")
    @GetMapping
    public ResponseEntity<List<TaleDto>> findTaleList() {
        List<TaleDto> taleDtoList = taleService.findTaleList();
        return ResponseEntity.ok()
                .body(taleDtoList);
    }

    @Operation(summary = "동화 상세 조회")
    @GetMapping("/{taleId}")
    public ResponseEntity<TaleDetailDto> findTale(@PathVariable Long taleId) {
        TaleDetailDto taleDetailDto = taleService.findTale(taleId);
        return ResponseEntity.ok()
                .body(taleDetailDto);
    }

//    /**
//     * 이름으로 검색
//     */
//    @GetMapping("/search")
//    public ResponseEntity<?> searchTale(HttpServletRequest request, @RequestParam(required = false) String title) {
//        try {
//            if(title == null || title.trim().isEmpty()) {
//                throw new IllegalArgumentException("검색어를 입력하세요.");
//            }
//
//            List<TaleDto> list = taleService.selectByTitle(title);
//
//            if(list.isEmpty()) {
//                throw new NoTalesFoundException("일치하는 결과를 찾을 수 없습니다.");
//            } else {
//                return new ResponseEntity<>(list, HttpStatus.OK);
//            }
//        } catch (IllegalArgumentException  e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        } catch ( NoTalesFoundException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
//    }
}