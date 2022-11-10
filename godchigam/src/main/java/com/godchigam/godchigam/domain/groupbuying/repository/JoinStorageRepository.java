package com.godchigam.godchigam.domain.groupbuying.repository;

import com.godchigam.godchigam.domain.groupbuying.entity.JoinPeople;
import com.godchigam.godchigam.domain.groupbuying.entity.JoinStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JoinStorageRepository extends JpaRepository<JoinStorage,Long> {

    /**
     * product 등록시 -> joinStorage 자동으로 생성되어야함. 등록 api 구현에서 넣을것
     *
     */
    @Query("select b from JoinStorage b where b.product.productIdx= :productId ")
    Optional<JoinStorage> findByProduct(@Param("productId") Long productId);

    @Query("select b from JoinPeople b where b.joinStorage.joinStorageIdx = :joinStorageIdx ")
    List<JoinPeople> findByJoinStorageIdx(@Param("JoinStorageIdx") Long joinStorageIdx);
}
