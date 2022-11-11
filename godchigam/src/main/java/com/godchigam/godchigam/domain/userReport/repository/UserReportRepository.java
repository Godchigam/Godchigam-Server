package com.godchigam.godchigam.domain.userReport.repository;

import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.domain.userReport.model.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepository extends JpaRepository<UserReport,Long> {

}
