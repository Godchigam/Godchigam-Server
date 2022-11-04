package com.godchigam.godchigam.domain.recipesBookmark.service;

import com.godchigam.godchigam.domain.recipesBookmark.dto.BookmarkResponse;
import com.godchigam.godchigam.domain.recipesBookmark.dto.RecipeInfoResponseDto;
import com.godchigam.godchigam.domain.recipesBookmark.model.Bookmark;
import com.godchigam.godchigam.domain.recipesBookmark.repository.BookmarkRepository;
import com.godchigam.godchigam.domain.recipes.repository.recipesRepository;
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
    private final recipesRepository recipesRepository;
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
        List<Recipes> recipes = recipesRepository.findAll();

        List<Bookmark> bookmarkStatus = bookmarkRepository.getBookmarkStatus(userId);
        List<Wish> wishStatus = wishRepository.getWishStatus(userId);

        List<Long> recipesIdList = new ArrayList<>();
        List<Long> recipesIdList2 = new ArrayList<>();
        bookmarkStatus.forEach(bookmark -> {
            recipesIdList.add(
                    bookmark.getRecipes().getIdx()
            );
        });
        wishStatus.forEach(wish -> {
            recipesIdList2.add(
                    wish.getRecipes().getIdx()
            );
        });


        recipes = recipes.stream().distinct().collect(Collectors.toList());
        List<RecipeInfoResponseDto> newList = new ArrayList<>();

        recipes.forEach(recipes1 -> {

                        bookmarkStatus.forEach(bookmark -> {
                            if (recipesIdList.contains(recipes1.getIdx()) && bookmark.getRecipes().getIdx() == recipes1.getIdx()) {
                                 //   wishStatus.forEach(wish -> {
                                Boolean isLike = Boolean.TRUE;
                                            newList.add(
                                                    RecipeInfoResponseDto.builder()
                                                            .recipeId(recipes1.getIdx())
                                                            .recipeImageUrl(recipes1.getImage_url())
                                                            .recipeTitle(recipes1.getName())
                                                            .dishes(recipes1.getDish())
                                                            .time(recipes1.getCookingTime())
                                                            .difficulty(recipes1.getDifficulty())
                                                            .likeCount(recipes1.getCountOfLikes())
                                                            .isBookmarked(Boolean.TRUE)
                                                            .isLiked(isLike)
                                                            .build()
                                            );

                            }
                        }  );}
                            );


        return newList;


    }

    }






