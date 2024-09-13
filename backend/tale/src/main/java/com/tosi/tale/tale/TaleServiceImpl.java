package com.tosi.tale.tale;

import com.tosi.tale.common.exception.CustomException;
import com.tosi.tale.common.exception.ExceptionCode;
import com.tosi.tale.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaleServiceImpl implements TaleService {
    private final TaleRepository taleRepository;
    private final S3Service s3Service;
    private final Josa josa = new Josa();

    /**
     * 특정 페이지의 동화 목록을 TaleDto 객체 리스트로 반환합니다.
     *
     * @param pageable 페이지 번호, 페이지 크기, 정렬 기준 및 방향을 담고 있는 Pageable 객체
     * @return TaleDto 객체 리스트를 감싼 TaleDtos 객체
     * @throws CustomException 동화 목록이 없을 경우 예외 처리
     */
    @Cacheable(value = "taleListCache", key = "#pageable.pageNumber")
    @Override
    public TaleDto.TaleDtos findTaleList(Pageable pageable) {
        List<TaleDto> taleDtoList = taleRepository.findTaleList(pageable)
                .orElseThrow(() -> new CustomException(ExceptionCode.ALL_TALES_NOT_FOUND));

        return new TaleDto.TaleDtos(
                taleDtoList.stream()
                        .map(taleDto -> taleDto.withThumbnailS3URL(
                                s3Service.findS3URL(taleDto.getThumbnailS3Key()))
                        )
                        .toList()
        );
    }

    /**
     * 동화 본문, 등장인물, 삽화 등을 포함한 상세 정보를 TaleDetailDto 객체로 반환합니다.
     *
     * @param taleId Tale 객체 id
     * @return TaleDetailDto 객체
     * @throws CustomException 해당 id의 동화가 없을 경우 예외 처리
     */
    @Cacheable(value = "taleCache", key = "#taleId")
    @Override
    public TaleDetailDto findTale(Long taleId) {
        TaleDetailS3Dto taleDetailS3Dto = taleRepository.findTale(taleId)
                .orElseThrow(() -> new CustomException(ExceptionCode.TALE_NOT_FOUND));

        String content = s3Service.findContents(taleDetailS3Dto.getContentS3Key());
        String[] characters = s3Service.findCharacters(taleDetailS3Dto.getContentS3Key());
        List<String> images = s3Service.findImages(taleDetailS3Dto.getImageS3KeyPrefix());

        return TaleDetailDto.builder()
                .taleId(taleDetailS3Dto.getTaleId())
                .title(taleDetailS3Dto.getTitle())
                .content(content)
                .characters(characters)
                .images(images)
                .build();
    }

    /**
     * 제목의 일부를 포함하는 동화 목록을 TaleDto 객체 리스트로 반환합니다.
     *
     * @param titlePart 검색할 동화 제목 일부
     * @param pageable  페이지 번호, 페이지 크기, 정렬 기준 및 방향을 담고 있는 Pageable 객체
     * @return 검색된 제목을 포함하는 TaleDto 객체 리스트
     */
    @Override
    public List<TaleDto> findTaleByTitle(String titlePart, Pageable pageable) {
        return taleRepository.findTaleByTitle(titlePart, pageable).stream()
                .map(t -> t.withThumbnailS3URL(
                        s3Service.findS3URL(t.getThumbnailS3Key())))
                .toList();
    }

    /**
     * 등장인물의 이름을 사용자 지정 이름으로 교체하고, 각 삽화와 교체된 본문을 매칭하여 동화를 구성합니다.
     *
     * @param talePageRequestDto 동화 본문, 삽화 정보, 이름 맵 등이 포함된 TalePageRequestDto 객채
     * @return TalePageResponseDto 리스트
     */
    @Override
    public List<TalePageResponseDto> createTalePages(TalePageRequestDto talePageRequestDto) {
        if(talePageRequestDto == null)
            throw new CustomException(ExceptionCode.PAGE_REQUEST_NOT_FOUND);

        String changedContent = replaceToUserName(
                talePageRequestDto.getTaleDetailDto().getContent(),
                talePageRequestDto.getTaleDetailDto().getCharacters(),
                talePageRequestDto.getNameMap()
        );
        String[] splitContent = changedContent.split("-----");
        List<TalePageResponseDto> talePageResponseDtoList = matchImagesWithContent(
                splitContent,
                talePageRequestDto.getTaleDetailDto().getImages()
        );

        return talePageResponseDtoList;
    }


    /**
     * 동화 등장인물 이름을 사용자가 지정한 이름으로 교체합니다.
     *
     * @param content    동화 본문
     * @param characters 등장인물 목록
     * @param nameMap    등장인물 이름과 사용자 이름을 매핑한 맵
     * @return 등장인물 이름을 지정 이름과 그에 맞는 조사로 교체한 동화 본문
     */
    private String replaceToUserName(String content, String[] characters, LinkedHashMap<String, String> nameMap) {
        StringBuilder sb = new StringBuilder(content);

        for (String character : characters) {
            String user = nameMap.get(character);
            if (character.equals(user)) continue;

            int characterIdx = sb.indexOf(character); // 등장인물 이름 인덱스
            while (characterIdx != -1) {
                int josaIdx = characterIdx + character.length(); // 조사 인덱스
                String currJosa = (josaIdx < sb.length()) ? sb.substring(josaIdx, josaIdx + 1) : ""; // 조사

                String updatedNameAndJosa = user + josa.appendJosa(user, currJosa); // 사용자 이름 + 새로운 조사 문자열
                sb.replace(characterIdx, josaIdx + 1, updatedNameAndJosa);  // 등장인물 이름 + 기존 조사를 위 문자열로 교체

                characterIdx = sb.indexOf(character, characterIdx + updatedNameAndJosa.length());
            }
        }

        return sb.toString();
    }

    /**
     * 이름이 매핑된 동화로 페이지를 생성합니다.
     * 왼쪽 페이지는 삽화가 포함되고, 오른쪽 페이지는 동화 본문을 2문장씩 삽입합니다.
     *
     * @param splitContent 한 삽화에 대응하는 본문 내용 배열
     * @param images 삽화 주소 리스트
     * @return TalePageResponseDto 객체 리스트
     */
    private List<TalePageResponseDto> matchImagesWithContent(String[] splitContent, List<String> images) {
        int pageNum = 1;
        List<TalePageResponseDto> talePageResponseDtoList = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {
            String currImgS3URL = images.get(i);
            String[] lines = splitContent[i].split("\n"); // 문장 단위 배열

            for (int j = 0; j < lines.length; j += 2) {
                String line1 = lines[j];
                // line1이 마지막 문장이면 다음 문장은 빈 문장
                String line2 = (j + 1 < lines.length) ? lines[j + 1] : "";

                talePageResponseDtoList.add(
                        TalePageResponseDto.builder()
                                .leftNo(pageNum++)
                                .left(currImgS3URL)
                                .rightNo(pageNum++)
                                .right(line1 + "\n" + line2)
                                .build()
                );
            }

        }

        return talePageResponseDtoList;
    }

}
