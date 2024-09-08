package com.tosi.tale.taleDetail;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.tosi.favorite.QFavorite;
import com.ssafy.tosi.tale.QTale;
import org.springframework.transaction.annotation.Transactional;

public class TaleDetailRepositoryCustomImpl implements TaleDetailRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TaleDetailRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    @Transactional
    public int updateLikeCnt(int taleId) {
        QTale qtale = QTale.tale;
        QFavorite qFavorite = QFavorite.favorite;

        Long likeCnt = queryFactory.select(qFavorite.favoriteId.count())
                .from(qFavorite)
                .where(qFavorite.taleId.eq(taleId))
                .fetchOne();


        int updatedLikeCnt = likeCnt != null ? likeCnt.intValue() : 0;

        return (int) queryFactory.update(qtale)
                .set(qtale.likeCnt, updatedLikeCnt)
                .where(qtale.taleId.eq(taleId))
                .execute();

    }
}
