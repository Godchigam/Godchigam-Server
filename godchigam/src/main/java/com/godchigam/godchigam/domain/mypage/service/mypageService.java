package com.godchigam.godchigam.domain.mypage.service;

import com.godchigam.godchigam.domain.mypage.dto.MypageRequest;
import com.godchigam.godchigam.domain.mypage.dto.MypageResponse;

import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.domain.user.repository.UserRepository;
import com.godchigam.godchigam.domain.mypage.repository.MypageRepository;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor

public class mypageService {
    private final UserRepository userRepository;
    private final MypageRepository mypageRepository;


    public MypageResponse checkMypage(String userId) {
        Optional<User> user = userRepository.findByLoginId(userId);

        if (user.isEmpty()) {
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        }
        User newUser = user.get();


        return MypageResponse.builder()
                .nickname(newUser.getNickname())
                .profileImageUrl(newUser.getProfileImageUrl())
                .address(newUser.getAddress())
                .build();
    }


    public MypageResponse updateMypage(String id, MypageRequest request){

        Optional<User> optionalMypage = mypageRepository.findByLoginId(id);
        if(!optionalMypage.isPresent()){
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        }

        User user = optionalMypage.get();

        user.setNickname(request.getNickname());
        user.setProfileImageUrl(request.getProfileImageUrl());
        user.setAddress(request.getAddress());

        userRepository.save(user);

        return MypageResponse.builder()
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .address(user.getAddress())
                .build();
    }


}