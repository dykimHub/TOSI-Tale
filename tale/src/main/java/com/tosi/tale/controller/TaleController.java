package com.tosi.tale.controller;

import com.tosi.tale.dto.TaleDetailDto;
import com.tosi.tale.dto.TaleDto;
import com.tosi.tale.dto.TalePageRequestDto;
import com.tosi.tale.dto.TalePageResponseDto;
import com.tosi.tale.service.TaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<TaleDto.TaleDtos> findTaleList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "regDate") String sort,
            @RequestParam(defaultValue = "desc") String dir
    ) {
        Pageable pageable = PageRequest.of(page, 9, Sort.by(Sort.Direction.fromString(dir), sort));
        TaleDto.TaleDtos taleDtoList = taleService.findTaleList(pageable);
        return ResponseEntity.ok()
                .body(taleDtoList);
    }

    @Operation(summary = "동화 개요 조회")
    @GetMapping("/{taleId}")
    public ResponseEntity<TaleDto> findTaleList(@Parameter(example = "6") @PathVariable Long taleId) {
        TaleDto taleDto = taleService.findTale(taleId);
        return ResponseEntity.ok()
                .body(taleDto);
    }

    @Operation(summary = "동화 내용 조회")
    @GetMapping("/content/{taleId}")
    public ResponseEntity<TaleDetailDto> findTaleDetail(@Parameter(example = "6") @PathVariable Long taleId) {
        TaleDetailDto taleDetailDto = taleService.findTaleDetail(taleId);
        return ResponseEntity.ok()
                .body(taleDetailDto);
    }

    @Operation(summary = "동화 제목으로 검색")
    @GetMapping("/search")
    public ResponseEntity<List<TaleDto>> findTaleByTitle(
            @Parameter(example = "여우") @RequestParam String titlePart,
            @RequestParam(defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, 9, Sort.by("title"));
        List<TaleDto> taleDtoList = taleService.findTaleByTitle(titlePart, pageable);
        return ResponseEntity.ok()
                .body(taleDtoList);
    }

    @Operation(summary = "등장인물 이름을 매핑하고 각 페이지 생성")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            value = ExampleObject.taleDetailExample
                    )
            )
    )
    @PostMapping("/read")
    public ResponseEntity<List<TalePageResponseDto>> createTalePages(@RequestHeader("Authorization") String accessToken, @RequestBody TalePageRequestDto talePageRequestDto) {
        Long userId = taleService.findUserAuthorization(accessToken);
        List<TalePageResponseDto> talePageResponseDtoList = taleService.createTalePages(talePageRequestDto);
        return ResponseEntity.ok()
                .body(talePageResponseDtoList);
    }


}