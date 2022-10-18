package com.godchigam.godchigam.global.auth;

import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.domain.user.repository.UserRepository;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;


    public String SignUpUser(AuthRequest authRequest) {

        User newUser = new User();
        BeanUtils.copyProperties(authRequest, newUser);
        newUser.setStatus("ACTIVE");
        userRepository.save(newUser);

        return "회원 가입에 성공했습니다.";
    }


}
