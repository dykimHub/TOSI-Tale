package com.tosi.tale.taleDetail;

import com.ssafy.tosi.tale.Tale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaleDetailRepository extends JpaRepository<Tale, Integer>, TaleDetailRepositoryCustom {


}
