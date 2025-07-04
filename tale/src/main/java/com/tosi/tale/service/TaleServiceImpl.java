package com.tosi.tale.service;

import com.tosi.common.client.ApiClient;
import com.tosi.common.client.ApiUtils;
import com.tosi.common.constants.ApiPaths;
import com.tosi.common.constants.CachePrefix;
import com.tosi.common.dto.TaleBaseDto;
import com.tosi.common.dto.TaleCacheDto;
import com.tosi.common.dto.TaleDetailCacheDto;
import com.tosi.common.dto.TalePageDto;
import com.tosi.common.exception.CustomException;
import com.tosi.common.service.CacheService;
import com.tosi.tale.dto.TaleDetailDto;
import com.tosi.tale.dto.TaleDto;
import com.tosi.tale.dto.TalePageRequestDto;
import com.tosi.tale.exception.ExceptionCode;
import com.tosi.tale.repository.TaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaleServiceImpl implements TaleService {

    private final TaleRepository taleRepository;
    private final S3Service s3Service;
    private final JosaService josaService;
    private final CacheService cacheService;
    private final ApiClient apiClient;
    @Value("${service.user.url}")
    private String userURL;

    /**
     * 정렬 조건에 대한 특정 페이지의 동화 목록을 반환합니다.
     * 각 페이지에 해당하는 동화 ID를 캐시에서 조회합니다.
     * 캐시에 없으면 DB에서 조회한 후 캐시에 15분 동안 등록합니다.
     *
     * @param pageable 페이지 번호, 페이지 크기, 정렬 기준 및 방향을 담고 있는 Pageable 객체
     * @return TaleCacheDto 객체 리스트
     * @throws CustomException 동화 목록이 없을 경우 예외 처리
     */
    @Override
    public List<TaleCacheDto> findTaleList(Pageable pageable) {
        String cacheKey = CachePrefix.TALE_LIST.buildCacheKey(Long.valueOf(pageable.getPageNumber()));
        List<Long> taleIds = cacheService.getCache(cacheKey, List.class)
                .orElseGet(() -> {
                            List<Long> newTaleIds = taleRepository.findTaleIdList(pageable);
                            if (newTaleIds.isEmpty()) throw new CustomException(ExceptionCode.PARTIAL_TALES_NOT_FOUND);
                            cacheService.setCache(cacheKey, newTaleIds, 15, TimeUnit.MINUTES);
                            return newTaleIds;
                        }
                );
        return findMultiTales(taleIds, 15, TimeUnit.MINUTES);
    }

    /**
     * 주어진 동화 ID 리스트에 대해 동화 정보를 조회합니다.
     * 회원 서비스에서 요청한 동화 정보는 1시간 동안 캐싱합니다.
     *
     * @param taleIds Tale 객체 id 목록
     * @return TaleCacheDTO 객체 리스트
     */
    @Override
    public List<TaleCacheDto> findMultiTales(List<Long> taleIds) {
        return findMultiTales(taleIds, 1, TimeUnit.HOURS);
    }

    /**
     * 검색된 제목의 일부를 포함하는 해당 페이지의 동화 목록을 TaleCacheDto 객체 리스트로 반환합니다.
     * 제목 검색으로 반환된 동화 객체 목록은 30분만 캐싱합니다.
     *
     * @param titlePart 검색할 동화 제목 일부
     * @param pageable  페이지 번호, 페이지 크기, 정렬 기준 및 방향을 담고 있는 Pageable 객체
     * @return TaleCacheDto 객체 리스트
     */
    @Override
    public List<TaleCacheDto> findTaleByTitle(String titlePart, Pageable pageable) {
        List<Long> taleIds = taleRepository.findTaleByTitle(titlePart, pageable);
        return findMultiTales(taleIds, 30, TimeUnit.MINUTES);
    }

    /**
     * 동화 ID 리스트에 대해 캐싱된 동화 객체를 조회합니다.
     * 캐시에 없는 동화는 DB에서 조회한 후 S3 URL을 세팅하고 캐시를 업데이트합니다.
     *
     * @param taleIds Tale 객체 id 목록
     * @param timeout 캐시 만료 시간
     * @param unit    캐시 단위
     * @return TaleCacheDto 리스트
     * @throws CustomException DB에서 동화 목록을 찾을 수 없으면 예외 처리
     */
    private List<TaleCacheDto> findMultiTales(List<Long> taleIds, long timeout, TimeUnit unit) {
        // Redis에서 캐시 키로 동화 캐시를 조회
        List<TaleCacheDto> cachedTaleDtoList = cacheService.getMultiCaches(CachePrefix.TALE.buildCacheKeys(taleIds), TaleCacheDto.class);
        // 캐시 미스가 없다면, 그대로 캐시된 결과를 반환
        if (taleIds.size() == cachedTaleDtoList.size())
            return cachedTaleDtoList;
        // 캐싱된 동화 객체 Map(key: 동화 Id, value: 동화 객체)
        Map<Long, TaleCacheDto> cachedTaleDtoMap = createTaleDtoMap(cachedTaleDtoList);
        // 캐싱되지 않은 동화 객체를 DB에서 조회
        List<Long> missingTaleIdList = cacheService.findMissingIdList(taleIds, cachedTaleDtoMap);
        List<TaleDto> missingTaleList = taleRepository.findMultiTales(missingTaleIdList);
        if (missingTaleList.isEmpty())
            throw new CustomException(ExceptionCode.PARTIAL_TALES_NOT_FOUND);
        // DB에서 조회한 동화 객체 Map(key: 동화 Id, value: 동화 객체)
        Map<Long, TaleCacheDto> missingTaleDtoMap = createTaleDtoMap(missingTaleList);
        // 캐싱용 Map(key: 캐시 키, value: 동화 객체)
        Map<String, TaleCacheDto> cacheDtoMap = cacheService.createCacheMap(missingTaleDtoMap, CachePrefix.TALE);
        // 주어진 시간 동안 동화 캐시 일괄 저장
        cacheService.setMultiCaches(cacheDtoMap, timeout, unit);
        // 캐시에 있던 동화 객체와 DB에 있던 동화 객체 리스트 순서대로 반환
        return cacheService.mergedCachedAndMissing(taleIds, cachedTaleDtoMap, missingTaleDtoMap);
    }

    /**
     * TaleBaseDto를 상속하는 객체 리스트를 받아서 TaleCacheDto 맵으로 변환합니다.
     *
     * @param taleDtoList 동화 객체(TaleDto, TaleCacheDto) 리스트
     * @param <T>         TaleBaseDto를 상속하는 모든 클래스 타입 가능
     * @return key: 동화 Id, value: TaleCacheDto 객체인 Map
     */
    private <T extends TaleBaseDto> Map<Long, TaleCacheDto> createTaleDtoMap(List<T> taleDtoList) {
        return taleDtoList.stream().collect(Collectors.toMap(
                TaleBaseDto::getTaleId,
                t -> t.getThumbnailS3Key() == null ? (TaleCacheDto) t : convertToTaleCacheDto(t)
        ));
    }

    /**
     * 동화 본문, 등장인물, 삽화 등을 포함한 동화 상세 정보를 반환합니다.
     * 해당 동화 Id를 캐시에서 조회합니다.
     * 캐시에 없으면 DB, S3에서 필요한 정보를 조회한 다음 TaleDetail 객체를 생성하고 15분 동안 캐시에 등록합니다.
     *
     * @param taleId Tale 객체 id
     * @return TaleDetailCacheDto 객체
     */
    @Override
    public TaleDetailCacheDto findTaleDetail(Long taleId) {
        String cacheKey = CachePrefix.TALE_DETAIL.buildCacheKey(taleId);
        TaleDetailCacheDto taleDetailCacheDto = cacheService.getCache(cacheKey, TaleDetailCacheDto.class)
                .orElseGet(() -> {
                            TaleDetailDto taleDetailDto = taleRepository.findTaleDetail(taleId)
                                    .orElseThrow(() -> new CustomException(ExceptionCode.TALE_NOT_FOUND));
                            TaleDetailCacheDto newTaleDetailCacheDto = convertToTaleDetailCacheDto(taleDetailDto);
                            cacheService.setCache(cacheKey, newTaleDetailCacheDto, 15, TimeUnit.MINUTES);
                            return newTaleDetailCacheDto;

                        }
                );

        return taleDetailCacheDto;

    }

    /**
     * 주어진 동화 ID 리스트에 대해 캐시에서 동화 상세 정보를 조회합니다.
     * 동화 상세 객체 리스트는 크기가 커서 인기 동화만 1시간 주기로 캐싱합니다.
     *
     * @param taleIds Tale 객체 id 목록
     * @return TaleDetailCacheDTO 객체 리스트
     * @throws CustomException 해당 id 목록의 동화가 DB에 없을 경우 예외 처리
     */
    @Override
    public List<TaleDetailCacheDto> findMultiTaleDetails(List<Long> taleIds) {
        return findMultiTaleDetails(taleIds, 1, TimeUnit.HOURS);
    }

    /**
     * 동화 ID 리스트에 대해 캐싱된 동화 상세 객체를 조회합니다.
     * 캐시에 없는 동화는 DB에서 조회한 후 S3 URL을 세팅하고 캐시를 업데이트합니다.
     *
     * @param taleIds 동화 객체 id 목록
     * @param timeout 캐시 만료 시간
     * @param unit    시간 단위
     * @return 동화 상세 객체 리스트
     */
    private List<TaleDetailCacheDto> findMultiTaleDetails(List<Long> taleIds, long timeout, TimeUnit unit) {
        // Redis에서 캐시 키로 동화 상세 캐시를 조회
        List<TaleDetailCacheDto> cachedTaleDetailDtoList = cacheService.getMultiCaches(CachePrefix.TALE_DETAIL.buildCacheKeys(taleIds), TaleDetailCacheDto.class);
        // 모든 객체가 존재하면 그대로 캐시된 결과를 반환
        if (taleIds.size() == cachedTaleDetailDtoList.size())
            return cachedTaleDetailDtoList;
        // 캐싱된 동화 상세 객체 Map(key: 동화 Id, value: 동화 상세 객체)
        Map<Long, TaleDetailCacheDto> cachedTaleDetailMap = createTaleDetailDtoMap(cachedTaleDetailDtoList);
        // 캐싱되지 않은 동화 상세 객체를 DB에서 조회
        List<Long> missingTaleIds = cacheService.findMissingIdList(taleIds, cachedTaleDetailMap);
        List<TaleDetailDto> missingTaleDetailDtoList = taleRepository.findMultiTaleDetails(missingTaleIds);
        if (missingTaleDetailDtoList.isEmpty())
            throw new CustomException(ExceptionCode.PARTIAL_TALES_NOT_FOUND);
        // DB에서 조회한 동화 상세 객체 Map(key: 동화 Id, value: 동화 상세 객체)
        Map<Long, TaleDetailCacheDto> missingTaleDetailMap = createTaleDetailDtoMap(missingTaleDetailDtoList);
        // 캐싱용 Map(key: 캐시 키, value: 동화 상세 객체)
        Map<String, TaleDetailCacheDto> cacheDtoMap = cacheService.createCacheMap(missingTaleDetailMap, CachePrefix.TALE_DETAIL);
        // 주어진 시간 동안 동화 상세 캐시 일괄 저장
        cacheService.setMultiCaches(cacheDtoMap, timeout, unit);
        return cacheService.mergedCachedAndMissing(taleIds, cachedTaleDetailMap, missingTaleDetailMap);

    }

    /**
     * TaleBaseDto를 상속하는 객체 리스트를 받아서 TaleDetailCacheDto 맵으로 변환합니다.
     *
     * @param taleDetailDtoList 동화 객체(TaleDetailDto, TaleDetailCacheDto) 리스트
     * @param <T>               TaleBaseDto를 상속하는 모든 클래스 타입 가능
     * @return key: 동화 Id, value: TaleCacheDto 객체인 Map
     */
    private <T extends TaleBaseDto> Map<Long, TaleDetailCacheDto> createTaleDetailDtoMap(List<T> taleDetailDtoList) {
        return taleDetailDtoList.stream().collect(Collectors.toMap(
                TaleBaseDto::getTaleId,
                t -> t.getContentS3Key() == null ? (TaleDetailCacheDto) t : convertToTaleDetailCacheDto(t)
        ));
    }


    /**
     * 등장인물의 이름을 사용자 지정 이름으로 교체하고, 각 삽화와 교체된 본문을 매칭하여 동화를 구성합니다.
     *
     * @param talePageRequestDto 동화 본문, 삽화 정보, 이름 맵 등이 포함된 TalePageRequestDto 객채
     * @return TalePageResponseDto 리스트
     * @throws CustomException talePageRequestDto 객체가 비어있는 경우 예외 처리
     */
    @Override
    public List<TalePageDto> createTalePages(TalePageRequestDto talePageRequestDto) {
        if (talePageRequestDto == null)
            throw new CustomException(ExceptionCode.PAGE_REQUEST_NOT_FOUND);

        // 등장인물 이름을 사용자 이름으로 변환
        String changedContent = replaceToUserName(
                talePageRequestDto.getContent(),
                talePageRequestDto.getCharacters(),
                talePageRequestDto.getNameMap()
        );

        return ApiUtils.createTalePages(changedContent.split("-----"), talePageRequestDto.getImages());
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
        Long userId = apiClient.getObject(ApiPaths.AUTH.buildPath(userURL), headers, Long.class);
        return userId;

    }

    private TaleCacheDto convertToTaleCacheDto(TaleBaseDto taleBaseDto) {
        return TaleCacheDto.builder()
                .taleId(taleBaseDto.getTaleId())
                .title(taleBaseDto.getTitle())
                .thumbnailS3URL(s3Service.findS3URL(taleBaseDto.getThumbnailS3Key())) // 썸네일 주소
                .ttsLength(taleBaseDto.getTtsLength())
                .build();
    }

    private TaleDetailCacheDto convertToTaleDetailCacheDto(TaleBaseDto taleBaseDto) {
        return TaleDetailCacheDto.builder()
                .taleId(taleBaseDto.getTaleId())
                .title(taleBaseDto.getTitle())
                .content(s3Service.findContents(taleBaseDto.getContentS3Key())) // 동화 본문
                .characters(s3Service.findCharacters(taleBaseDto.getContentS3Key())) // 등장인물
                .images(s3Service.findImages(taleBaseDto.getImageS3KeyPrefix())) // 삽화 주소 목록
                .build();
    }


}
