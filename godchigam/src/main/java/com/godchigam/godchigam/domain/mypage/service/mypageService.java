package com.godchigam.godchigam.domain.mypage.service;

import com.godchigam.godchigam.domain.mypage.dto.mypageResponse;

import com.godchigam.godchigam.domain.recipesBookmark.repository.BookmarkRepository;
import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.domain.user.repository.UserRepository;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class mypageService {
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;

    public mypageResponse checkMypage(String userId) {
        Optional<User> user = userRepository.findByLoginId(userId);
        //  List<Recipes> recipes = bookmarkRepository.getRecipesList(userId, status);

        if (user.isEmpty()) {
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        }
        User newUser = user.get();


        return mypageResponse.builder()
                .nickname(newUser.getNickname())
                .profileImageUrl(newUser.getProfileImageUrl())
                //주소는 아직 user에 없음
                .build();
    }
}