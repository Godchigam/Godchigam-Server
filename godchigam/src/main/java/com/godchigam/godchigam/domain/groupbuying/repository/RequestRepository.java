package com.godchigam.godchigam.domain.groupbuying.repository;

import com.godchigam.godchigam.domain.groupbuying.entity.RequestMessage;
import com.godchigam.godchigam.domain.groupbuying.entity.RequestStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<RequestStorage,Long> {

    @Query("select b from RequestStorage b where b.user.loginId= :loginId ")
    Optional<RequestStorage> findByUser(@Param("loginId") String loginId);

    @Query("select b from RequestMessage b where b.requestStorage.requestStorageIdx= :requestStorageIdx ")
    List<RequestMessage> findByRequestStorageIdx(@Param("requestStorageIdx") Long requestStorageIdx);

    @Query("select b from RequestMessage b where b.requestStorage.requestStorageIdx= :requestStorageIdx and b.requestSendUser.loginId= :loginId")
    Optional<RequestMessage> findByRequestStorageIdxAndSenderLoginId(@Param("requestStorageIdx") Long requestStorageIdx,@Param("loginId") String loginId);

    @Query("select b from RequestMessage b where b.requestStorage.requestStorageIdx= :requestStorageIdx and b.requestIdx= :requestId")
    Optional<RequestMessage> findByRequestStorageIdxAndRequestMessageIdx(@Param("requestStorageIdx") Long requestStorageIdx,@Param("loginId") Long requestId);

}
