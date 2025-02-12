package com.tosi.tale.service;

import com.tosi.common.cache.CachePrefix;
import com.tosi.common.cache.CacheService;
import com.tosi.common.cache.TaleCacheDto;
import com.tosi.common.cache.TaleDetailCacheDto;
import com.tosi.common.exception.CustomException;
import com.tosi.tale.dto.*;
import com.tosi.tale.exception.ExceptionCode;
import com.tosi.tale.repository.TaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaleServiceImpl implements TaleService {

    private final TaleRepository taleRepository;
    private final S3Service s3Service;
    private final JosaService josaService;
    private final RestTemplate restTemplate;
    private final CacheService cacheService;
    @Value("${service.user.url}")
    private String userURL;

    /**
     * 특정 페이지의 동화 목록을 TaleDto 객체 리스트로 반환합니다.
     * 페이지별 동화 목록(#페이지번호)을 캐시에 등록합니다.
     *
     * @param pageable 페이지 번호, 페이지 크기, 정렬 기준 및 방향을 담고 있는 Pageable 객체
     * @return TaleDto 객체 리스트를 감싼 TaleDtos 객체
     * @throws CustomException 동화 목록이 없을 경우 예외 처리
     */
    @Cacheable(value = "taleListCache", key = "#pageable.pageNumber")
    @Override
    public TaleDto.TaleDtos findTaleList(Pageable pageable) {
        List<TaleDto> taleDtoList = taleRepository.findTaleList(pageable);

        if (taleDtoList.isEmpty())
            throw new CustomException(ExceptionCode.ALL_TALES_NOT_FOUND);

        return new TaleDto.TaleDtos(
                taleDtoList.stream()
                        .map(taleDto -> taleDto.withThumbnailS3URL(
                                s3Service.findS3URL(taleDto.getThumbnailS3Key()))
                        )
                        .toList()
        );
    }

    /**
     * 동화 제목, 동화 표지, TTS 구연 시간 등을 포함한 동화 정보를 반환합니다.
     * 캐시가 존재하면 캐시를 반환하고 없다면 DB에서 TaleDto 객체를 조회하여 캐시에 등록하고 반환합니다.
     *
     * @param taleId Tale 객체 id
     * @return TaleCacheDTO 객체
     * @throws CustomException 해당 id의 동화가 없을 경우 예외 처리
     */
    @Override
    public TaleCacheDto findTale(Long taleId) {
        String cacheKey = CachePrefix.TALE.buildCacheKey(taleId);
        TaleCacheDto taleCacheDto = cacheService.getCache(cacheKey, TaleCacheDto.class);

        if (taleCacheDto != null)
            return taleCacheDto;

        TaleDto taleDto = taleRepository.findTale(taleId)
                .orElseThrow(() -> new CustomException(ExceptionCode.TALE_NOT_FOUND));

        String thumbnailS3URL = s3Service.findS3URL(taleDto.getThumbnailS3Key());
        TaleCacheDto newTaleCacheDto = taleDto.withThumbnailS3URL(thumbnailS3URL).toTaleCacheDto();

        cacheService.setCache(cacheKey, newTaleCacheDto, 6, TimeUnit.HOURS);

        return newTaleCacheDto;

    }

    /**
     * 주어진 동화 ID 리스트에 대해 캐시에서 동화 정보를 조회합니다.
     * 캐시에 없는 동화는 DB에서 조회한 후 S3 URL을 세팅하고 캐시를 업데이트합니다.
     *
     * @param taleIds Tale 객체 id 목록
     * @return TaleCacheDTO 객체 리스트
     */
    @Override
    public List<TaleCacheDto> findMultiTales(List<Long> taleIds) {
        // 동화 ID 리스트를 캐시 키 리스트로 변환합니다.
        List<String> cacheKeys = taleIds.stream()
                .map(CachePrefix.TALE::buildCacheKey)
                .toList();

        // Redis에서 캐시 키로 동화 캐시를 조회하고 가변 리스트로 만듭니다.
        List<TaleCacheDto> taleCacheDtos = new ArrayList<>(cacheService.getMultiCaches(cacheKeys, TaleCacheDto.class));

        // 맵에 캐시에서 조회되지 않은(=null) 동화 ID와 인덱스(순서 유지용)를 저장합니다.
        // key: 동화 ID, value: taleCacheDtos 리스트 내 인덱스
        Map<Long, Integer> missingTaleIndexMap = new HashMap<>();

        for (int i = 0; i < taleCacheDtos.size(); i++) {
            if (taleCacheDtos.get(i) == null) {
                missingTaleIndexMap.put(taleIds.get(i), i);
            }
        }

        // 캐시 미스가 없다면, 그대로 캐시된 결과를 반환합니다.
        if (missingTaleIndexMap.isEmpty())
            return taleCacheDtos;

        // 캐시에 없는 동화 ID 리스트 대해 DB에서 동화 정보를 조회합니다.
        List<TaleDto> taleDtos = taleRepository.findMultiTales(new ArrayList<>(missingTaleIndexMap.keySet()));

        // DB에서 조회한 동화 정보를 동화 ID를 key로 하는 맵으로 생성합니다.
        // S3에서 썸네일 URL을 세팅하고, TaleCacheDto로 형변환 합니다.
        Map<Long, TaleCacheDto> taleDtoMap = taleDtos.stream()
                .collect(Collectors.toMap(
                        TaleDto::getTaleId, // key
                        t -> t.withThumbnailS3URL(s3Service.findS3URL(t.getThumbnailS3Key())).toTaleCacheDto()) // value
                );

        // DB에서 조회한 동화 정보를 taleCacheDtos 리스트에 채웁니다.
        // 캐시 업데이트용 Map에 담습니다.
        Map<String, Object> newTaleCacheDtoMap = new HashMap<>();

        for (Map.Entry<Long, Integer> entry : missingTaleIndexMap.entrySet()) {
            long missingTaleId = entry.getKey();
            int missingIndex = entry.getValue();

            TaleCacheDto taleCacheDto = taleDtoMap.get(missingTaleId);
            if (taleCacheDto == null)
                throw new CustomException(ExceptionCode.TALE_NOT_FOUND);

            taleCacheDtos.set(missingIndex, taleCacheDto);
            newTaleCacheDtoMap.put(CachePrefix.TALE.buildCacheKey(missingTaleId), taleCacheDto);

        }

        // 동화 캐시를 일괄 업데이트합니다.
        cacheService.setMultiCaches(newTaleCacheDtoMap, 6, TimeUnit.HOURS);

        // 캐시에 있던 동화 객체와 DB에 있던 동화 객체가 합쳐져 최종적으로 반환됩니다.
        return taleCacheDtos;
    }

    /**
     * 동화 본문, 등장인물, 삽화 등을 포함한 상세 정보를 TaleDetailDto 객체로 반환합니다.
     *
     * @param taleId Tale 객체 id
     * @return TaleDetailDto 객체
     * @throws CustomException 해당 id의 동화가 없을 경우 예외 처리
     */
    @Override
    public TaleDetailDto findTaleDetail(Long taleId) {
        TaleDetailCacheDto taleDetailCacheDto = cacheService.getCache(CachePrefix.TALE_DETAIL.buildCacheKey(taleId), TaleDetailCacheDto.class);
        if (taleDetailCacheDto != null) {
            return TaleDetailDto.of(taleDetailCacheDto.getTaleId(), taleDetailCacheDto.getTitle(), taleDetailCacheDto.getContent(), taleDetailCacheDto.getCharacters(), taleDetailCacheDto.getImages());
        }

        TaleDetailS3Dto taleDetailS3Dto = taleRepository.findTaleDetail(taleId)
                .orElseThrow(() -> new CustomException(ExceptionCode.TALE_NOT_FOUND));

        String content = s3Service.findContents(taleDetailS3Dto.getContentS3Key());
        String[] characters = s3Service.findCharacters(taleDetailS3Dto.getContentS3Key());
        List<String> images = s3Service.findImages(taleDetailS3Dto.getImageS3KeyPrefix());

        return TaleDetailDto.of(taleDetailS3Dto.getTaleId(), taleDetailS3Dto.getTitle(), content, characters, images);
    }

    /**
     * 주어진 동화 ID 리스트에 대해 캐시에서 동화 상세 정보를 조회합니다.
     * 동화 상세 객체는 크기가 커서 인기 동화만 1시간 주기로 캐싱합니다.
     * 따라서 하나라도 캐시에 없으면 만료된 것으로 간주하고 DB에서 다시 조회하여 최신 인기 동화 상세를 캐시에 저장합니다.
     *
     * @param taleIds Tale 객체 id 목록
     * @return TaleDetailCacheDTO 객체 리스트
     */
    @Override
    public List<TaleDetailCacheDto> findMultiTaledetails(List<Long> taleIds) {
        // 동화 ID 리스트를 캐시 키 리스트로 변환합니다.
        List<String> cacheKeys = taleIds.stream()
                .map(CachePrefix.TALE_DETAIL::buildCacheKey)
                .toList();

        // Redis에서 캐시 키로 동화 상세 캐시를 조회합니다.
        List<TaleDetailCacheDto> taleDetailCacheDtos = cacheService.getMultiCaches(cacheKeys, TaleDetailCacheDto.class);

        // 모든 객체가 존재하면 그대로 캐시된 결과를 반환합니다.
        boolean hasNull = taleDetailCacheDtos.stream().anyMatch(Objects::isNull); // 하나라도 null이면 true
        if (!hasNull)
            return taleDetailCacheDtos;

        // 캐시가 존재하지 않으면 동화 ID 목록을 DB에서 조회합니다.
        List<TaleDetailS3Dto> taleDetailS3Dtos = taleRepository.findMultiTaleDetails(taleIds);

        // S3에서 동화 본문, 등장인물, 삽화 URL 목록을 가져와 TaleDetailCacheDto 객체 리스트를 생성합니다.
        List<TaleDetailCacheDto> newTaleDetailCacheDtos = taleDetailS3Dtos.stream()
                .map(t -> {
                    String content = s3Service.findContents(t.getContentS3Key());
                    String[] characters = s3Service.findCharacters(t.getContentS3Key());
                    List<String> images = s3Service.findImages(t.getImageS3KeyPrefix());

                    return TaleDetailDto.of(t.getTaleId(), t.getTitle(), content, characters, images) // TaleDetailDto 객체 생성
                            .toTaleDetailCacheDto(); // TaleDetailCacheDto로 변환
                })
                .toList();

        // 새로 조회한 동화 상세 정보를 Map으로 변환합니다.
        Map<String, Object> newTaleDetailCacheDtoMap = newTaleDetailCacheDtos.stream()
                .collect(Collectors.toMap(
                        t -> CachePrefix.TALE_DETAIL.buildCacheKey(t.getTaleId()),
                        t -> t
                ));

        // 동화 상세 캐시를 일괄 업데이트합니다.
        cacheService.setMultiCaches(newTaleDetailCacheDtoMap, 1, TimeUnit.HOURS);

        return newTaleDetailCacheDtos;
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
        if (talePageRequestDto == null)
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

                String updatedNameAndJosa = user + josaService.appendJosa(user, currJosa); // 사용자 이름 + 새로운 조사 문자열
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
     * @param images       삽화 주소 리스트
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

    /**
     * 회원 서비스로 토큰을 보내고 인증이 완료되면 회원 번호를 반환합니다.
     *
     * @param accessToken 로그인한 회원의 토큰
     * @return 회원 번호
     * @throws CustomException 인증에 성공하지 못하면 예외 처리
     */
    @Override
    public Long findUserAuthorization(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        try {
            Long userId = restTemplate.exchange(userURL + "/auth",
                    HttpMethod.GET, httpEntity, Long.class).getBody();
            return userId;
        } catch (Exception e) {
            throw new CustomException(ExceptionCode.INVALID_TOKEN);
        }
    }


}
