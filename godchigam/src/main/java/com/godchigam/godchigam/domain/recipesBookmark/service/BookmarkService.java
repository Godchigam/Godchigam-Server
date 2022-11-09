package com.godchigam.godchigam.domain.recipesBookmark.service;

import com.godchigam.godchigam.domain.recipesBookmark.dto.BookmarkResponse;
import com.godchigam.godchigam.domain.recipesBookmark.dto.RecipeInfoResponseDto;
import com.godchigam.godchigam.domain.recipesBookmark.model.Bookmark;
import com.godchigam.godchigam.domain.recipesBookmark.repository.BookmarkRepository;
import com.godchigam.godchigam.domain.recipes.repository.RecipesRepository;
import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import com.godchigam.godchigam.domain.recipesWish.model.Wish;
import com.godchigam.godchigam.domain.recipesWish.repository.WishRepository;
import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.domain.user.repository.UserRepository;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookmarkService {
    private final RecipesRepository recipesRepository;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;
    private final WishRepository wishRepository;


    //북마크 체크하기
    @Transactional
    public BookmarkResponse checkBookmark(String userId, Long recipesIdx){
        Optional<User> user=userRepository.findByLoginId(userId);
        Optional<Recipes> recipes = recipesRepository.findById(recipesIdx);
        Optional<Bookmark> bookmarks = bookmarkRepository.getBookmark(userId,recipesIdx);


        if(user.isEmpty()){
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        }
        if(recipes.isEmpty()){
            throw new BaseException(ErrorCode.RECIPES_EMPTY);
        }
        if(bookmarks.isEmpty()){
            User newUser1 = user.get();
            Recipes newRecipes1 = recipes.get();
            BeanUtils.copyProperties(bookmarks,newRecipes1);
            Bookmark newbb = new Bookmark(Boolean.TRUE, newUser1, newRecipes1);
            bookmarkRepository.save(newbb);
            return BookmarkResponse.builder()
                    .isBookmarked(newbb.getStatus())
                    .build();

        }

        else{
            Boolean newbookmark = bookmarks.get().getStatus();
            Optional<Bookmark> bookmark = bookmarkRepository.getBookmark(userId,recipesIdx);

            log.info("북마크 이미 있음");
            Bookmark updateBookmark = bookmark.get();
            if(updateBookmark.getStatus().equals(Boolean.FALSE)){
                updateBookmark.setStatus(bookmarks.get().updateStatusToTrue(newbookmark));
                bookmarkRepository.save(updateBookmark);
            }else{
                updateBookmark.setStatus(bookmarks.get().updateStatusToFalse(newbookmark));
                bookmarkRepository.save(updateBookmark);
            }
            return BookmarkResponse.builder()
                    .isBookmarked(updateBookmark.getStatus())
                    .build();
        }
    }

    //내가 북마크한 레시피 불러오기
    @Transactional
    public List<RecipeInfoResponseDto> readBookmark(String userId, Boolean status){
        List<Bookmark> bookmarkStatus = bookmarkRepository.getBookmarkStatus(userId);
        List<Wish> wishStatus = wishRepository.getWishStatus(userId);

        List<Long> recipesIdList2 = new ArrayList<>();

        wishStatus.forEach(wish -> {
            recipesIdList2.add(
                    wish.getRecipes().getIdx()
            );
        });

        List<RecipeInfoResponseDto> newList = new ArrayList<>();

        bookmarkStatus.forEach(recipes1 -> {
            Boolean isLike = recipesIdList2.contains(recipes1.getRecipes().getIdx());
            newList.add(RecipeInfoResponseDto.builder()
                    .recipeId(recipes1.getRecipes().getIdx())
                    .recipeImageUrl(recipes1.getRecipes().getImage_url())
                    .recipeTitle(recipes1.getRecipes().getName())
                    .dishes(recipes1.getRecipes().getDish())
                    .time(recipes1.getRecipes().getCookingTime())
                    .difficulty(recipes1.getRecipes().getDifficulty())
                    .likeCount(recipes1.getRecipes().getCountOfLikes())
                    .isBookmarked(recipes1.getStatus())
                    .isLiked(isLike)
                    .build())
            ;
        });


        return newList;


    }

    }






