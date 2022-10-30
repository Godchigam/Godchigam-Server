package com.godchigam.godchigam.domain.recipesWish.service;


import com.godchigam.godchigam.domain.recipesWish.dto.WishResponse;
import com.godchigam.godchigam.domain.recipesWish.model.Wish;
import com.godchigam.godchigam.domain.recipesWish.model.WishStatus;
import com.godchigam.godchigam.domain.recipesWish.repository.WishRepository;
import com.godchigam.godchigam.domain.recipes.repository.recipesRepository;
import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.domain.user.repository.UserRepository;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor

public class WishService {
    private final recipesRepository recipesRepository;
    private final UserRepository userRepository;
    private final WishRepository wishRepository;

    public WishResponse checkWish(String userId, Long recipesIdx){
        Optional<User> user=userRepository.findByLoginId(userId);
        Optional<Recipes> recipes = recipesRepository.findById(recipesIdx);
        if(user.isEmpty()){
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        }
        if(recipes.isEmpty()){
            throw new BaseException(ErrorCode.REQUEST_ERROR);
        }
        User newUser = user.get();
        Recipes newRecipes = recipes.get();
        Optional<Wish> wish = wishRepository.getWish(userId,recipesIdx);
        if(wish.isEmpty()){
            log.info("북마크 없음");
            Wish newwish = new Wish(WishStatus.ON, newUser, newRecipes);
            wishRepository.save(newwish);

            return WishResponse.builder()
                    .userId(userId)
                    .recipesId(recipesIdx)
                    .name(newRecipes.getName())
                    .status(newwish.getStatus())
                    .build();
        }else{
            log.info("북마크 이미 있음");
            Wish updateWish = wish.get();
            if(updateWish.getStatus().equals(WishStatus.OFF)){
                updateWish.setStatus(WishStatus.ON);
                wishRepository.save(updateWish);
            }else{ //OFF
                updateWish.setStatus(WishStatus.OFF);
                wishRepository.save(updateWish);
            }
            return WishResponse.builder()
                    .userId(userId)
                    .recipesId(recipesIdx)
                    .name(updateWish.getRecipes().getName())
                    .status(updateWish.getStatus())
                    .build();
        }
    }

}
