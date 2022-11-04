package com.godchigam.godchigam.domain.recipes.service;


import com.godchigam.godchigam.domain.recipes.dto.RecipeDetailResponse;
import com.godchigam.godchigam.domain.recipes.dto.RecipesFindResponse;
import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import com.godchigam.godchigam.domain.recipes.repository.recipesRepository;
import com.godchigam.godchigam.domain.recipesBookmark.dto.RecipeInfoResponseDto;
import com.godchigam.godchigam.domain.recipesBookmark.model.Bookmark;
import com.godchigam.godchigam.domain.recipesBookmark.repository.BookmarkRepository;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;

@Service
@Slf4j
@RequiredArgsConstructor
public class recipesService {

    private final recipesRepository recipesRepository;
    private final BookmarkRepository bookmarkRepository;

    //레시피 전체 조회
    @Transactional
    public List<RecipeInfoResponseDto> readRecipes(String userId){
        List<Recipes> recipes = recipesRepository.findAll();
        List<Bookmark> bookmarkStatus = bookmarkRepository.getBookmarkStatus(userId);

        List<Long> recipesIdList = new ArrayList<>();
        bookmarkStatus.forEach(bookmark -> {
            recipesIdList.add(
                    bookmark.getRecipes().getIdx()
            );
        });

        recipes = recipes.stream().distinct().collect(Collectors.toList());
        List<RecipeInfoResponseDto> newList = new ArrayList<>();

        recipes.forEach(recipes1 -> {

             //   if(recipesIdList.contains(recipes1.getIdx())){
                 //   bookmarkStatus.forEach(bookmark -> {
                    //    if(bookmark.getRecipes().getIdx()==recipes1.getIdx()){
                      //      Boolean b = bookmark.getStatus();
                            newList.add(
                                    RecipeInfoResponseDto.builder()
                                            .recipeId(recipes1.getIdx())
                                            .recipeImageUrl(recipes1.getImage_url())
                                            .recipeTitle(recipes1.getName())
                                            .dishes(recipes1.getDish())
                                            .time(recipes1.getCookingTime())
                                            .difficulty(recipes1.getDifficulty())
                                            .likeCount(recipes1.getCountOfLikes())
                                            .isBookmarked(TRUE)
                                            .isLiked(TRUE)
                                            .build()
                            );
                        //}

             //       });
           //     }



                }
        );
        return newList;


    }

    //레시피 카테고리 조회
    @Transactional
    public List<RecipeInfoResponseDto> searchRecipes(String keyword){
        List<Recipes> recipes = recipesRepository.findByCategoryContaining(keyword);
        if(recipes.isEmpty()){
            throw new BaseException(ErrorCode.RECIPES_EMPTY);
        }

        recipes = recipes.stream().distinct().collect(Collectors.toList());
        List<RecipeInfoResponseDto> newList = new ArrayList<>();
        recipes.forEach(recipes1 -> {
                    newList.add(
                            RecipeInfoResponseDto.builder()
                                    .recipeId(recipes1.getIdx())
                                    .recipeImageUrl(recipes1.getImage_url())
                                    .recipeTitle(recipes1.getName())
                                    .dishes(recipes1.getDish())
                                    .time(recipes1.getCookingTime())
                                    .difficulty(recipes1.getDifficulty())
                                    .likeCount(recipes1.getCountOfLikes())
                                    .isBookmarked(TRUE)
                                    .isLiked(TRUE)
                                    .build()
                    );
                }
        );
        return newList;
    }

    //레시피 상세조회
    @Transactional
    public RecipeDetailResponse readRecipe(Long id){
        Optional<Recipes> recipes = recipesRepository.findById(id);
        if(recipes.isEmpty()){
            throw new BaseException(ErrorCode.RECIPES_EMPTY);
        }

        return RecipeDetailResponse.builder()
                .recipeInfo(RecipeInfoResponseDto.builder()
                        .recipeId(recipes.get().getIdx())
                        .recipeImageUrl(recipes.get().getImage_url())
                        .recipeTitle(recipes.get().getName())
                        .dishes(recipes.get().getDish())
                        .time(recipes.get().getCookingTime())
                        .difficulty(recipes.get().getDifficulty())
                        .likeCount(recipes.get().getCountOfLikes())
                        .isBookmarked(TRUE)
                        .isLiked(TRUE)
                        .build()
                )
                .ingredients(recipes.get().getIngredient())
                .orders(recipes.get().getCooking_method())
                .build();

    }

    //레시피 필터링 난이도 낮은 순서 && 전체조회
    @Transactional
    public List<RecipeInfoResponseDto> readAllDifficulty(String userId){
        List<Recipes> recipes = recipesRepository.findAll();
        List<Recipes> recipes2 = recipesRepository.findByDifficultyContaining("아무나");
        List<Recipes> recipes3 = recipesRepository.findByDifficultyContaining("초급");
        List<Recipes> recipes4 = recipesRepository.findByDifficultyContaining("중급");

        List<Bookmark> bookmarkStatus = bookmarkRepository.getBookmarkStatus(userId);

        List<Long> recipesIdList = new ArrayList<>();
        bookmarkStatus.forEach(bookmark -> {
            recipesIdList.add(
                    bookmark.getRecipes().getIdx()
            );
        });

        recipes = recipes.stream().distinct().collect(Collectors.toList());
        List<RecipeInfoResponseDto> newList = new ArrayList<>();
        List<Recipes> recipes5 = new ArrayList<>();
        recipes5.addAll(recipes2);
        recipes5.addAll(recipes3);
        recipes5.addAll(recipes4);

        recipes5.forEach(recipes1 -> {
                    newList.add(
                            RecipeInfoResponseDto.builder()
                                    .recipeId(recipes1.getIdx())
                                    .recipeImageUrl(recipes1.getImage_url())
                                    .recipeTitle(recipes1.getName())
                                    .dishes(recipes1.getDish())
                                    .time(recipes1.getCookingTime())
                                    .difficulty(recipes1.getDifficulty())
                                    .likeCount(recipes1.getCountOfLikes())
                                    .isBookmarked(TRUE)
                                    .isLiked(TRUE)
                                    .build()
                    );

                }
        );

        return newList;
    }

    //레시피 필터링 난이도 낮은 순서 && 카테고리 조회
    @Transactional
    public List<RecipeInfoResponseDto> readCategoryDifficulty(String userId, String category){

        List<Recipes> recipes2 = recipesRepository.findByDifficultyContainingAndCategoryContaining("아무나",category);
        List<Recipes> recipes3 = recipesRepository.findByDifficultyContainingAndCategoryContaining("초급",category);
        List<Recipes> recipes4 = recipesRepository.findByDifficultyContainingAndCategoryContaining("중급", category);

        List<Bookmark> bookmarkStatus = bookmarkRepository.getBookmarkStatus(userId);

        List<Long> recipesIdList = new ArrayList<>();
        bookmarkStatus.forEach(bookmark -> {
            recipesIdList.add(
                    bookmark.getRecipes().getIdx()
            );
        });

        List<RecipeInfoResponseDto> newList = new ArrayList<>();
        List<Recipes> recipes5 = new ArrayList<>();
        recipes5.addAll(recipes2);
        recipes5.addAll(recipes3);
        recipes5.addAll(recipes4);

        recipes5.forEach(recipes1 -> {
                    newList.add(
                            RecipeInfoResponseDto.builder()
                                    .recipeId(recipes1.getIdx())
                                    .recipeImageUrl(recipes1.getImage_url())
                                    .recipeTitle(recipes1.getName())
                                    .dishes(recipes1.getDish())
                                    .time(recipes1.getCookingTime())
                                    .difficulty(recipes1.getDifficulty())
                                    .likeCount(recipes1.getCountOfLikes())
                                    .isBookmarked(TRUE)
                                    .isLiked(TRUE)
                                    .build()
                    );

                }
        );

        return newList;
    }

    //레시피 필터링 시간 적은 순서 && 전체조회
    @Transactional
    public List<RecipeInfoResponseDto> readAllTime(String userId){
        List<Recipes> recipes = recipesRepository.findAll();
        List<Recipes> recipes2 = recipesRepository.findByCookingTimeStartsWith("5분 이내");
        List<Recipes> recipes3 = recipesRepository.findByCookingTimeContaining("10분 이내");
        List<Recipes> recipes4 = recipesRepository.findByCookingTimeContaining("15분 이내");
        List<Recipes> recipes5 = recipesRepository.findByCookingTimeContaining("20분 이내");
        List<Recipes> recipes6 = recipesRepository.findByCookingTimeContaining("30분 이내");
        List<Recipes> recipes7 = recipesRepository.findByCookingTimeContaining("60분 이내");
        log.info(String.valueOf(recipes2.size()));
        log.info(String.valueOf(recipes3.size()));
        log.info(String.valueOf(recipes4.size()));
        log.info(String.valueOf(recipes5.size()));
        log.info(String.valueOf(recipes6.size()));
        log.info(String.valueOf(recipes7.size()));


        List<Bookmark> bookmarkStatus = bookmarkRepository.getBookmarkStatus(userId);

        List<Long> recipesIdList = new ArrayList<>();
        bookmarkStatus.forEach(bookmark -> {
            recipesIdList.add(
                    bookmark.getRecipes().getIdx()
            );
        });

        List<RecipeInfoResponseDto> newList = new ArrayList<>();
        List<Recipes> recipes10 = new ArrayList<>();
        recipes10.addAll(recipes2);
        recipes10.addAll(recipes3);
        recipes10.addAll(recipes4);
        recipes10.addAll(recipes5);
        recipes10.addAll(recipes6);
        recipes10.addAll(recipes7);

        recipes10.forEach(recipes1 -> {
                    newList.add(
                            RecipeInfoResponseDto.builder()
                                    .recipeId(recipes1.getIdx())
                                    .recipeImageUrl(recipes1.getImage_url())
                                    .recipeTitle(recipes1.getName())
                                    .dishes(recipes1.getDish())
                                    .time(recipes1.getCookingTime())
                                    .difficulty(recipes1.getDifficulty())
                                    .likeCount(recipes1.getCountOfLikes())
                                    .isBookmarked(TRUE)
                                    .isLiked(TRUE)
                                    .build()
                    );

                }
        );

        return newList;
    }

    //레시피 필터링 시간적은 순서 && 카테고리 조회
    @Transactional
    public List<RecipeInfoResponseDto> readCategoryTime(String userId, String category){

        List<Recipes> recipes2 = recipesRepository.findByCookingTimeStartsWithAndCategoryContaining("5분 이내",category);
        List<Recipes> recipes3 = recipesRepository.findByCookingTimeContainingAndCategoryContaining("10분 이내",category);
        List<Recipes> recipes4 = recipesRepository.findByCookingTimeContainingAndCategoryContaining("15분 이내", category);
        List<Recipes> recipes5 = recipesRepository.findByCookingTimeContainingAndCategoryContaining("20분 이내", category);
        List<Recipes> recipes6 = recipesRepository.findByCookingTimeContainingAndCategoryContaining("30분 이내", category);
        List<Recipes> recipes7 = recipesRepository.findByCookingTimeContainingAndCategoryContaining("60분 이내", category);

        List<Bookmark> bookmarkStatus = bookmarkRepository.getBookmarkStatus(userId);

        List<Long> recipesIdList = new ArrayList<>();
        bookmarkStatus.forEach(bookmark -> {
            recipesIdList.add(
                    bookmark.getRecipes().getIdx()
            );
        });

        List<RecipeInfoResponseDto> newList = new ArrayList<>();
        List<Recipes> recipes8 = new ArrayList<>();
        recipes8.addAll(recipes2);
        recipes8.addAll(recipes3);
        recipes8.addAll(recipes4);
        recipes8.addAll(recipes5);
        recipes8.addAll(recipes6);
        recipes8.addAll(recipes7);

        recipes8.forEach(recipes1 -> {
                    newList.add(
                            RecipeInfoResponseDto.builder()
                                    .recipeId(recipes1.getIdx())
                                    .recipeImageUrl(recipes1.getImage_url())
                                    .recipeTitle(recipes1.getName())
                                    .dishes(recipes1.getDish())
                                    .time(recipes1.getCookingTime())
                                    .difficulty(recipes1.getDifficulty())
                                    .likeCount(recipes1.getCountOfLikes())
                                    .isBookmarked(TRUE)
                                    .isLiked(TRUE)
                                    .build()
                    );

                }
        );

        return newList;
    }

    //레시피 전체 조회, 좋아요 많은 순서
    @Transactional
    public List<RecipeInfoResponseDto> readAllLikes(String userId){
        List<Recipes> recipes = recipesRepository.findAll(Sort.by(Sort.Direction.DESC, "likeCount"));

        recipes = recipes.stream().distinct().collect(Collectors.toList());
        List<RecipeInfoResponseDto> newList = new ArrayList<>();

        recipes.forEach(recipes1 -> {

                    newList.add(
                            RecipeInfoResponseDto.builder()
                                    .recipeId(recipes1.getIdx())
                                    .recipeImageUrl(recipes1.getImage_url())
                                    .recipeTitle(recipes1.getName())
                                    .dishes(recipes1.getDish())
                                    .time(recipes1.getCookingTime())
                                    .difficulty(recipes1.getDifficulty())
                                    .likeCount(recipes1.getCountOfLikes())
                                    .isBookmarked(TRUE)
                                    .isLiked(TRUE)
                                    .build()
                    );


                }
        );
        return newList;


    }

    //레시피 카테고리 조회, 좋아요 순
    @Transactional
    public List<RecipeInfoResponseDto> readCategoryLikes(String keyword){
        List<Recipes> recipes = recipesRepository.findByCategoryContainingOrderByLikeCountDesc(keyword);
        if(recipes.isEmpty()){
            throw new BaseException(ErrorCode.RECIPES_EMPTY);
        }

        recipes = recipes.stream().distinct().collect(Collectors.toList());
        List<RecipeInfoResponseDto> newList = new ArrayList<>();
        recipes.forEach(recipes1 -> {
                    newList.add(
                            RecipeInfoResponseDto.builder()
                                    .recipeId(recipes1.getIdx())
                                    .recipeImageUrl(recipes1.getImage_url())
                                    .recipeTitle(recipes1.getName())
                                    .dishes(recipes1.getDish())
                                    .time(recipes1.getCookingTime())
                                    .difficulty(recipes1.getDifficulty())
                                    .likeCount(recipes1.getCountOfLikes())
                                    .isBookmarked(TRUE)
                                    .isLiked(TRUE)
                                    .build()
                    );
                }
        );
        return newList;
    }

}
