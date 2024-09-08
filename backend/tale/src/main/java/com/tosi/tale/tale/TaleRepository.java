package com.tosi.tale.tale;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaleRepository extends JpaRepository<Tale, Integer> {

    List<Tale> findByTitleContaining(String title);


}
