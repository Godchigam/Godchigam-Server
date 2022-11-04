package com.godchigam.godchigam.domain.mypage.repository;

import com.godchigam.godchigam.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MypageRepository extends JpaRepository<User,Long>
{
    @Query("select b from User b where b.loginId= :userId")
    Optional<User> findByLoginId(@Param("userId") String userId);

}
