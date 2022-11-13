package com.godchigam.godchigam.domain.userReport.service;
import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.domain.user.repository.UserRepository;
import com.godchigam.godchigam.domain.userReport.dto.UserReportCreationRequest;
import com.godchigam.godchigam.domain.userReport.model.UserReport;
import com.godchigam.godchigam.domain.userReport.repository.UserReportRepository;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserReportService {
    private final UserReportRepository userReportRepository;
    private final UserRepository userRepository;
    //유저 신고
    public UserReport createReport(String userId, UserReportCreationRequest request){
        Optional<User> user = userRepository.findById(request.getReportId()); //신고할 유저
        Optional<User> user1 = userRepository.findByLoginId(userId); //신고한 유저


        if(!user.isPresent()){
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        }
        UserReport reportToCreate = new UserReport();

        BeanUtils.copyProperties(request, reportToCreate);

        reportToCreate.setUserId(user1.get().getUserIdx()); //신고한 유저
        reportToCreate.setUser(user.get());//신고당할 유저
        reportToCreate.setUserId(user1.get().getUserIdx());

        //2번이상 신고당한 reportId는 user table에서 status "INACTIVE"로 변환
        if(reportToCreate.getUser().getCountOfReports()>0){
            reportToCreate.getUser().reChangeStatus();
        }
        return userReportRepository.save(reportToCreate);
    }
}
