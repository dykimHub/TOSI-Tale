package com.tosi.tale.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tosi.tale.dto.QTaleDetailDto;
import com.tosi.tale.dto.QTaleDto;
import com.tosi.tale.dto.TaleDetailDto;
import com.tosi.tale.dto.TaleDto;
import com.tosi.tale.entity.QTale;
import com.tosi.tale.entity.Tale;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TaleRepositoryCustomImpl implements TaleRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /**
     * Tale 엔티티 리스트를 TaleDto 객체 리스트로 변환하여 반환합니다.
     *
     * @param pageable 페이지 번호, 페이지 크기, 정렬 기준 및 방향을 담고 있는 Pageable 객체
     * @return TaleDto 객체 리스트
     */
    @Override
    public List<Long> findTaleIdList(Pageable pageable) {
        QTale qTale = QTale.tale;
        // QTale의 엔티티 타입(Tale)과 테이블명을 참조하여 tales 테이블의 컬럼을 참조할 동적 경로 생성
        PathBuilder<Tale> pathBuilder = new PathBuilder<>(Tale.class, qTale.getMetadata().getName());
        // Pageable 객체의 Sort 정보를 QueryDSL에서 사용하는 OrderSpecifier로 변환
        List<OrderSpecifier> orders = getOrderSpecifiers(pageable.getSort(), pathBuilder);

        return queryFactory.select(qTale.taleId)
                .from(qTale)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .fetch();

    }

    /**
     * 해당 id의 Tale 엔티티를 TaleDto 객체로 변환하여 반환합니다.
     *
     * @param taleId Tale 객체 id
     * @return Optional로 감싼 TaleDtoImpl 객체
     */
    @Override
    public Optional<TaleDto> findTale(Long taleId) {
        QTale qTale = QTale.tale;
        return Optional.ofNullable(queryFactory.select(new QTaleDto(
                        qTale.taleId,
                        qTale.title,
                        qTale.thumbnailS3Key,
                        qTale.ttsLength))
                .from(qTale)
                .where(qTale.taleId.eq(taleId))
                .fetchOne()
        );
    }

    /**
     * 해당 id 목록의 Tale 엔티티를 SQL IN 절을 사용하여 한 번의 쿼리로 조회합니다.
     * TaleDto 객체로 변환하여 반환합니다.
     *
     * @param taleIds Tale 객체 id 리스트
     * @return TaleDto 객체 리스트
     */
    @Override
    public List<TaleDto> findMultiTales(List<Long> taleIds) {
        QTale qTale = QTale.tale;
        return queryFactory.select(new QTaleDto(
                        qTale.taleId,
                        qTale.title,
                        qTale.thumbnailS3Key,
                        qTale.ttsLength))
                .from(qTale)
                .where(qTale.taleId.in(taleIds))
                .fetch();

    }

    /**
     * 해당 id의 Tale 엔티티를 TaleDetailS3Dto 객체로 변환하여 반환합니다.
     *
     * @param taleId Tale 객체 id
     * @return Optional로 감싼 TaleDetailS3Dto 객체
     */
    @Override
    public Optional<TaleDetailDto> findTaleDetail(Long taleId) {
        QTale qTale = QTale.tale;
        return Optional.ofNullable(queryFactory.select(new QTaleDetailDto(
                        qTale.taleId,
                        qTale.title,
                        qTale.contentS3Key,
                        qTale.imagesS3KeyPrefix))
                .from(qTale)
                .where(qTale.taleId.eq(taleId))
                .fetchOne()
        );
    }

    /**
     * 해당 id 목록의 Tale 엔티티를 SQL IN 절을 사용하여 한 번의 쿼리로 조회합니다.
     * TaleDetailS3Dto 객체로 변환하여 반환합니다.
     *
     * @param taleIds Tale 객체 id 리스트
     * @return
     */
    @Override
    public List<TaleDetailDto> findMultiTaleDetails(List<Long> taleIds) {
        QTale qTale = QTale.tale;
        return queryFactory.select(new QTaleDetailDto(
                        qTale.taleId,
                        qTale.title,
                        qTale.contentS3Key,
                        qTale.imagesS3KeyPrefix))
                .from(qTale)
                .where(qTale.taleId.in(taleIds))
                .fetch();
    }

    /**
     * 검색된 Tale 엔티티 리스트를 TaleDtoImpl 객체 리스트로 변환하여 반환합니다.
     *
     * @param titlePart 검색할 동화 제목 일부
     * @param pageable  페이지 번호, 페이지 크기, 정렬 기준 및 방향을 담고 있는 Pageable 객체
     * @return TaleDto 객체 리스트(결과가 없을 경우 빈 리스트 반환)
     */
    @Override
    public List<Long> findTaleByTitle(String titlePart, Pageable pageable) {
        QTale qTale = QTale.tale;
        // QTale의 엔티티 타입(Tale)과 테이블명을 참조하여 tales 테이블의 컬럼을 참조할 동적 경로 생성
        PathBuilder<Tale> pathBuilder = new PathBuilder<>(Tale.class, qTale.getMetadata().getName());
        // Pageable 객체의 Sort 정보를 QueryDSL에서 사용하는 OrderSpecifier로 변환
        List<OrderSpecifier> orders = getOrderSpecifiers(pageable.getSort(), pathBuilder);

        return queryFactory.select(qTale.taleId)
                .from(qTale)
                .where(qTale.title.containsIgnoreCase(titlePart))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .fetch();
    }

    /**
     * Sort 객체(정렬 기준 리스트)를 OrderSpecifier 객체 리스트로 변환합니다.
     *
     * @param sort        Sort 객체
     * @param pathBuilder 정의된 경로
     * @return QueryDSL 쿼리에서 사용하는 정렬 순서를 나타내는 OrderSpecifier 객체 리스트
     */
    private List<OrderSpecifier> getOrderSpecifiers(Sort sort, PathBuilder<Tale> pathBuilder) {
        return sort.stream()
                .map(order -> new OrderSpecifier(
                        order.isAscending() ? Order.ASC : Order.DESC, // 필드의 정렬 방향 설정
                        pathBuilder.get(order.getProperty()) // 필드에 대한 경로 가져오기
                ))
                .toList();
    }

}
