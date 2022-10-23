package com.godchigam.godchigam.domain.refrigerator.repository;

import com.godchigam.godchigam.domain.refrigerator.entity.Ingredient;
import com.godchigam.godchigam.domain.refrigerator.entity.Refrigerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefrigeratorRepository extends JpaRepository<Refrigerator,Long> {

    @Query("select b from Refrigerator b where b.user.loginId= :loginId ")
    Optional<Refrigerator> findByUser(@Param("loginId") String loginId);

    @Query("select b from Ingredient b where b.refrigerator.frigeIdx= :frigeIdx ")
    List<Ingredient> findByFrigeIdx(@Param("frigeIdx") Long frigeIdx);

}
