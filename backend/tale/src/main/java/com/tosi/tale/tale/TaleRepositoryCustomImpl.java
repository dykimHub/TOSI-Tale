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


}
