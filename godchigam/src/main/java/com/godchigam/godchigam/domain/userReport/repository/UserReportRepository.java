package com.godchigam.godchigam.domain.userReport.repository;

import com.godchigam.godchigam.domain.groupbuying.entity.JoinStorage;
import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.domain.userReport.model.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserReportRepository extends JpaRepository<UserReport,Long> {

    @Query("select b from UserReport b where b.reportId = :userIdx ")
    List<UserReport> findByReportId(@Param("userIdx") Long userIdx);
}
