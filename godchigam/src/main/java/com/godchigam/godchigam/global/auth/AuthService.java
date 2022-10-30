package com.godchigam.godchigam.global.auth;

import com.godchigam.godchigam.domain.refrigerator.entity.Refrigerator;
import com.godchigam.godchigam.domain.refrigerator.repository.RefrigeratorRepository;
import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.domain.user.repository.UserRepository;
import com.godchigam.godchigam.global.auth.dto.*;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RefrigeratorRepository refrigeratorRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoMapClient kakaoMapClient;

    /**
     * TO-DO
     * 유저 주소 정보 추가
     */
    public String SignUpUser(AuthRequest authRequest) {

        User newUser = new User();
        BeanUtils.copyProperties(authRequest, newUser);
        newUser.setStatus("ACTIVE");
        userRepository.save(newUser);
        Refrigerator newFrige = new Refrigerator();
        newFrige.setUser(newUser);
        refrigeratorRepository.save(newFrige);

        return "회원 가입에 성공했습니다.";
    }

    public String UserIdDuplicateCheck(String loginId) {

        log.info(loginId);
        Optional<User> duplicateUser = userRepository.findByLoginId(loginId);
        if (duplicateUser.isPresent()) {
            return null;
        }
        return "중복된 아이디가 존재하지 않습니다.";
    }

    public CommonResponse UserLogin(LoginRequest loginRequest) {

        Optional<User> findUser = userRepository.findByLoginId(loginRequest.getLoginId());
        if (findUser.isEmpty()) {
            return CommonResponse.error(400, "존재하는 계정이 없습니다.");
        }
        if (!findUser.get().getPassword().equals(loginRequest.getPassword())) {
            return CommonResponse.error(400, "비밀번호가 맞지 않습니다.");
        }

        String loginId = findUser.get().getLoginId();
        String accessToken = jwtTokenProvider.generateToken(loginId);
        findUser.get().setAccessToken(accessToken);

        log.info("발급된 토큰 : "+accessToken);
        userRepository.flush();

        UserResponse loginUser = UserResponse.builder()
                .nickname(findUser.get().getNickname())
                .accessToken(accessToken)
                .build();
        return CommonResponse.success(loginUser, "로그인 성공");
    }

    public UserResponse GetCurrentUserInfo(String accessToken) {

        if (!jwtTokenProvider.isValidToken(accessToken)) {
            return null;
        }
        String loginId = jwtTokenProvider.getUserLoginId(accessToken);
        log.info("추출해낸 로그인 아이디:" + loginId);
        Optional<User> currentLoginUser = userRepository.findByLoginId(loginId);

        UserResponse loginUser = UserResponse.builder()
                .nickname(currentLoginUser.get().getNickname())
                .accessToken(accessToken)
                .build();

        return loginUser;
    }

    public AddressResponse ChangeAddress(String x, String y) {
        KakaoMapResponse kakaoMapResponse= kakaoMapClient.BringAddress(x,y); ;
        String address = kakaoMapResponse.getAddress();
        return AddressResponse.builder()
                .address_name(address).build();
    }
}
