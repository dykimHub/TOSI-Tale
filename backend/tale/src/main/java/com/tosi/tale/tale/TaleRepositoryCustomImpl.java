package com.tosi.tale.tale;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TaleRepositoryCustomImpl implements TaleRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /**
     * Tale 엔티티 리스트를 TaleDto 객체 리스트로 변환하여 반환합니다.
     *
     * @return Optional로 감싼 TaleDto 객체 리스트
     */
    @Override
    public Optional<List<TaleDto>> findTaleList() {
        QTale tale = QTale.tale;
        return Optional.ofNullable(queryFactory.select(new QTaleDto(
                        tale.taleId,
                        tale.title,
                        tale.thumbnailS3Key,
                        tale.time))
                .from(tale)
                .fetch());


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
                        tale.time))
                .from(tale)
                .where(tale.taleId.eq(taleId))
                .fetchOne());
    }


}
