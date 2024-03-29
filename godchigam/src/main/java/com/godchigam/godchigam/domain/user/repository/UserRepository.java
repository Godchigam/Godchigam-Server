package com.godchigam.godchigam.domain.user.repository;

import com.godchigam.godchigam.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserIdx(Long userIdx);
    Optional<User> findByLoginId(String loginId);


}
