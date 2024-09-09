package com.tosi.tale.tale;

import java.util.List;
import java.util.Optional;

public interface TaleRepositoryCustom {

    Optional<List<TaleDto>> findTaleList();
}
