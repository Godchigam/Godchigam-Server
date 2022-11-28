package com.godchigam.godchigam.domain.groupbuying.repository;

import com.godchigam.godchigam.domain.groupbuying.entity.JoinPeople;
import com.godchigam.godchigam.domain.groupbuying.entity.RequestMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestMessageRepository extends JpaRepository<RequestMessage,Long> {
    @Query("select b from RequestMessage b where b.requestProduct.productIdx = :productId ")
    RequestMessage findByProduct(@Param("productId") Long productId);

}
