package com.tosi.tale.tale;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TaleRepositoryCustomImpl implements TaleRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /**
     * Tale 엔티티 리스트를 TaleDto 객체 리스트로 변환하여 반환합니다.
     *
     * @return Optional로 감싼 TaleDto 객체 리스트
     */
    @Override
    public Optional<List<TaleDto>> findTaleList(Pageable pageable) {
        QTale tale = QTale.tale;
        // QTale의 엔티티 타입(Tale)과 테이블명을 참조하여 tales 테이블의 컬럼을 참조할 동적 경로 생성
        PathBuilder<Tale> pathBuilder = new PathBuilder<>(Tale.class, tale.getMetadata().getName());
        // Pageable 객체의 Sort 정보를 QueryDSL에서 사용하는 OrderSpecifier로 변환
        List<OrderSpecifier> orders = getOrderSpecifiers(pageable.getSort(), pathBuilder);

        return Optional.ofNullable(queryFactory.select(new QTaleDto(
                        tale.taleId,
                        tale.title,
                        tale.thumbnailS3Key,
                        tale.ttsLength))
                .from(tale)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .fetch()
        );
    }

    /**
     * 해당 id의 Tale 엔티티를 TaleDetailS3Dto 객체로 변환하여 반환합니다.
     *
     * @param taleId Tale 객체 id
     * @return Optional로 감싼 TaleDetailS3Dto 객체
     */
    @Override
    public Optional<TaleDetailS3Dto> findTale(Long taleId) {
        QTale tale = QTale.tale;
        return Optional.ofNullable(queryFactory.select(new QTaleDetailS3Dto(
                        tale.taleId,
                        tale.title,
                        tale.contentS3Key,
                        tale.imagesS3KeyPrefix,
                        tale.ttsLength))
                .from(tale)
                .where(tale.taleId.eq(taleId))
                .fetchOne()
        );
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
