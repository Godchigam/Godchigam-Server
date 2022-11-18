package com.godchigam.godchigam.domain.recipesWish.service;


import com.godchigam.godchigam.domain.recipesWish.dto.WishResponse;
import com.godchigam.godchigam.domain.recipesWish.model.Wish;
import com.godchigam.godchigam.domain.recipesWish.repository.WishRepository;
import com.godchigam.godchigam.domain.recipes.repository.RecipesRepository;
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

//좋아요 체크하기
public class WishService {
    private final RecipesRepository recipesRepository;
    private final UserRepository userRepository;
    private final WishRepository wishRepository;

    public WishResponse checkWish(String userId, Long recipesIdx){
        Optional<User> user=userRepository.findByLoginId(userId);
        Optional<Recipes> recipes = recipesRepository.findById(recipesIdx);
        Optional<Wish> wishes = wishRepository.getWish(userId, recipesIdx);
        if(user.isEmpty()){
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        }
        if(recipes.isEmpty()){
            throw new BaseException(ErrorCode.REQUEST_ERROR);
        }
        //맨 처음 좋아요 누를 때
        if(wishes.isEmpty()){
            User newUser1 = user.get();
            Recipes newRecipes1 = recipes.get();
            Wish newww = new Wish(Boolean.TRUE, newUser1, newRecipes1);

            wishRepository.save(newww);
            Optional<Wish> wish = wishRepository.getWish(userId,recipesIdx);
            Wish update = wish.get();
            update.getRecipes().setLikeCount(update.getRecipes().getCountOfLikes());
            wishRepository.save(update);
            return WishResponse.builder()
                    .isLiked(newww.getStatus())
                    .likeCount(newww.getRecipes().getCountOfLikes())
                    .build();
        }
        //좋아요 이미 누른거 취소하거나 다시 누를 때
        else{
            Optional<Wish> wish = wishRepository.getWish(userId,recipesIdx);


            log.info("like 이미 있음");
            Wish updateWish = wish.get();
            Recipes updateRecipes = recipes.get();

            //if(updateWish.getStatus().equals(Boolean.FALSE)){//좋아요취소->좋아요
           //     updateWish.setStatus(Boolean.TRUE);
            //    updateWish.getRecipes().setLikeCount(updateWish.getRecipes().getCountOfLikes());
             //   wishRepository.save(updateWish);
            //}
            if(updateWish.getStatus().equals(Boolean.TRUE)){//좋아요->좋아요 취소
                updateWish.setStatus(Boolean.FALSE);
                wishRepository.deleteById(updateWish.getWishIdx());
                if(updateWish.getStatus().equals("false")){
                    wishRepository.deleteAll();
                }
                log.info("좋아요 삭제");
                updateWish.getRecipes().setLikeCount(updateWish.getRecipes().getCountOfLikes());
                log.info(String.valueOf(updateWish.getRecipes().getCountOfLikes()));
                log.info(String.valueOf(updateRecipes.getLikeCount()));
                log.info("카운트 다시 반영");
               // wishRepository.save(updateWish);
                recipesRepository.save(updateRecipes);
                log.info("저장");
            }
            return WishResponse.builder()
                    .isLiked(updateWish.getStatus())
                    .likeCount(updateWish.getRecipes().getLikeCount())
                    .build();
        }
    }

}
