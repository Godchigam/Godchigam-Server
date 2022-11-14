package com.godchigam.godchigam.domain.groupbuying.repository;


import com.godchigam.godchigam.domain.groupbuying.entity.JoinPeople;
import com.godchigam.godchigam.domain.groupbuying.entity.JoinStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JoinPeopleRepository extends JpaRepository<JoinPeople,Long> {
    @Query("select b from JoinPeople b where b.joinStorage.joinStorageIdx = :storageId ")
    List<JoinPeople> findByJoinStorage(@Param("storageId") Long storageId);

    @Query("select b from JoinPeople b where b.joinStatus like '참여중%' and b.joinUserLoginId = :loginId")
    List<JoinPeople> findByJoinStatusAndJoinUserLoginId(@Param("loginId") String loginId);
}
