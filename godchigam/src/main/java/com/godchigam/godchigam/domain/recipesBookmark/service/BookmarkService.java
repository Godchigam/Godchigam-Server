package com.godchigam.godchigam.domain.recipesBookmark.service;

import com.godchigam.godchigam.domain.recipes.dto.RecipesFindResponse;
import com.godchigam.godchigam.domain.recipesBookmark.dto.BookmarkResponse;
import com.godchigam.godchigam.domain.recipesBookmark.model.Bookmark;
import com.godchigam.godchigam.domain.recipesBookmark.model.BookmarkStatus;
import com.godchigam.godchigam.domain.recipesBookmark.repository.BookmarkRepository;
import com.godchigam.godchigam.domain.recipes.repository.recipesRepository;
import com.godchigam.godchigam.domain.recipes.entity.Recipes;
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

public class BookmarkService {
    private final recipesRepository recipesRepository;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;

    public BookmarkResponse checkBookmark(String userId, Long recipesIdx){
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
        Optional<Bookmark> bookmark = bookmarkRepository.getBookmark(userId,recipesIdx);
        if(bookmark.isEmpty()){
            log.info("북마크 없음");
            Bookmark newBookmark = new Bookmark(BookmarkStatus.ON, newUser, newRecipes);
            bookmarkRepository.save(newBookmark);

            return BookmarkResponse.builder()
                    .userId(userId)
                    .recipesId(recipesIdx)
                    .name(newRecipes.getName())
                    .status(newBookmark.getStatus())
                    .build();
        }else{
            log.info("북마크 이미 있음");
            Bookmark updateBookmark = bookmark.get();
            if(updateBookmark.getStatus().equals(BookmarkStatus.OFF)){
                updateBookmark.setStatus(BookmarkStatus.ON);
                bookmarkRepository.save(updateBookmark);
            }else{ //OFF
                updateBookmark.setStatus(BookmarkStatus.OFF);
                bookmarkRepository.save(updateBookmark);
            }
            return BookmarkResponse.builder()
                    .userId(userId)
                    .recipesId(recipesIdx)
                    .name(updateBookmark.getRecipes().getName())
                    .status(updateBookmark.getStatus())
                    .build();
        }
    }

    ////

    public List<BookmarkResponse> readBookmark(String userId, BookmarkStatus status){
        Optional<User> user=userRepository.findByLoginId(userId);
        List<Recipes> recipes = bookmarkRepository.getRecipesList(userId, status);
        if(user.isEmpty()){
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        }
        if(recipes.isEmpty()){
            return null;
        }
        recipes = recipes.stream().distinct().collect(Collectors.toList());
        List<BookmarkResponse> newList = new ArrayList<>();
        recipes.forEach(recipes1 -> {
                    newList.add(
                            BookmarkResponse.builder()
                                    .userId(userId)
                                    .recipesId(recipes1.getIdx())
                                    .name(recipes1.getName())
                                    .status(status)
                                    .build()

                    );
                }
        );
        return newList;

    }

}
