package com.tosi.tale.tale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaleRepository extends JpaRepository<Tale, Integer>, TaleRepositoryCustom {

    


}
