package com.tosi.tale.tale;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequestMapping("/api/tales")
@RequiredArgsConstructor
@RestController
public class TaleController {
    private final TaleService taleService;

    @Operation(summary = "동화 목록 조회")
    @GetMapping
    public ResponseEntity<TaleDto.TaleDtos> findTaleList(@PageableDefault(size = 9, sort = "regDate") Pageable pageable) {
        // JPA에서 쿼리 파라미터를 읽어 Pageable 객체로 반환
        TaleDto.TaleDtos taleDtoList = taleService.findTaleList(pageable);
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

    @Operation(summary = "동화 제목으로 검색")
    @GetMapping("/search")
    public ResponseEntity<List<TaleDto>> findTaleByTitle(@RequestParam String titlePart, @PageableDefault(size = 9, sort = "title") Pageable pageable) {
        // JPA에서 쿼리 파라미터를 읽어 Pageable 객체로 반환
        List<TaleDto> taleDtoList = taleService.findTaleByTitle(titlePart, pageable);
        return ResponseEntity.ok()
                .body(taleDtoList);
    }

    @Operation(summary = "등장인물 이름을 매핑하고 각 페이지 생성")
    @PostMapping("/read")
    public ResponseEntity<List<TalePageResponseDto>> createTalePages(@RequestBody TalePageRequestDto talePageRequestDto) {
        List<TalePageResponseDto> talePageResponseDtoList = taleService.createTalePages(talePageRequestDto);
        return ResponseEntity.ok()
                .body(talePageResponseDtoList);
    }


}