package com.godchigam.godchigam.domain.recipes.service;


import com.godchigam.godchigam.domain.recipes.dto.RecipeDetailResponse;
import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import com.godchigam.godchigam.domain.recipes.repository.RecipesRepository;
import com.godchigam.godchigam.domain.recipesBookmark.dto.RecipeInfoResponseDto;
import com.godchigam.godchigam.domain.recipesBookmark.model.Bookmark;
import com.godchigam.godchigam.domain.recipesBookmark.repository.BookmarkRepository;
import com.godchigam.godchigam.domain.recipesWish.model.Wish;
import com.godchigam.godchigam.domain.recipesWish.repository.WishRepository;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class recipesService {

    private final RecipesRepository recipesRepository;
    private final BookmarkRepository bookmarkRepository;
    private final WishRepository wishRepository;
    ArrayList<String> category_list = new ArrayList<>(Arrays.asList("전체보기","초스피드","다이어트","야식","찌개","샐러드","디저트","면/만두","밑반찬","양식"));


    //레시피 조회
    @Transactional
    public List<RecipeInfoResponseDto> searchRecipes(Integer keywordNum, Integer filter, String userId){

            List<Recipes> recipes;
            String keyword = category_list.get(keywordNum);
            if(keyword.equals("전체보기")){
                if(filter==0){
                    recipes = recipesRepository.findAll(Sort.by(Sort.Direction.DESC, "likeCount"));
                    log.info("전체보기임!!!!!!!!!!");
                }
                else if(filter==1){
                    recipes = readAllDifficulty();
                }
                else{
                    recipes = readAllTime();
                }
            }
            //카테고리
            else {
                log.info(keyword);
                if(filter==0){
                    recipes = recipesRepository.findByCategoryContainingOrderByLikeCountDesc(keyword);
                    log.info(String.valueOf(recipes.size()));
                }
                else if(filter==1){
                    recipes = readCategoryDifficulty(keyword);
                }
                else{
                    recipes = readCategoryTime(keyword);

                }
            }

            List<RecipeInfoResponseDto> newList = new ArrayList<>();


            if(recipes.isEmpty()){
                return newList;
            }

            List<Bookmark> bookmarkStatus = bookmarkRepository.getBookmarkStatus(userId);
            List<Wish> wishStatus = wishRepository.getWishStatus(userId);
            List<Long> recipesIdList1 = new ArrayList<>();
            List<Long> recipesIdList2 = new ArrayList<>();

            bookmarkStatus.forEach(bookmark -> {
                recipesIdList1.add(
                        bookmark.getRecipes().getIdx()
                );
            });

            wishStatus.forEach(wish -> {
                recipesIdList2.add(
                     wish.getRecipes().getIdx()
                );
            });



            recipes.forEach(recipes1 -> {
                    Boolean isLiked = recipesIdList2.contains(recipes1.getIdx());
                    Boolean isBookmarked = recipesIdList1.contains(recipes1.getIdx());

                newList.add(
                            RecipeInfoResponseDto.builder()
                                    .recipeId(recipes1.getIdx())
                                    .recipeImageUrl(recipes1.getImage_url())
                                    .recipeTitle(recipes1.getName())
                                    .dishes(recipes1.getDish())
                                    .time(recipes1.getCookingTime())
                                    .difficulty(recipes1.getDifficulty())
                                    .likeCount(recipes1.getCountOfLikes())
                                    .isBookmarked(isBookmarked)
                                    .isLiked(isLiked)
                                    .build()
                    );
                }
        );
        return newList;
    //}
}

    //레시피 상세조회
    @Transactional
    public RecipeDetailResponse readRecipe(Long id, String userId){
        Optional<Recipes> recipes = recipesRepository.findById(id);
        if(recipes.isEmpty()){
            throw new BaseException(ErrorCode.RECIPES_EMPTY);
        }
        List<Bookmark> bookmarkStatus = bookmarkRepository.getBookmarkStatus(userId);
        List<Wish> wishStatus = wishRepository.getWishStatus(userId);
        String ingredients = recipesRepository.findById(id).get().getIngredient();
        String methods = recipesRepository.findById(id).get().getCooking_method();
        ingredients = ingredients.substring(0, ingredients.length()-1);
        ingredients = ingredients.substring(0,ingredients.length()-1);
        ingredients = ingredients.substring(1);
        ingredients = ingredients.substring(1);
        methods = methods.substring(0, methods.length()-1);
        methods = methods.substring(0,methods.length()-1);
        methods = methods.substring(1);
        methods = methods.substring(1);

        String[] ingredients_arr = ingredients.split("', '");
        String[] methods_arr = methods.split("', '");

        List<String> ingredients_list = Arrays.asList(ingredients_arr);
        List<String> methods_list = Arrays.asList(methods_arr);

        List<Long> recipesIdList1 = new ArrayList<>();
        List<Long> recipesIdList2 = new ArrayList<>();


        bookmarkStatus.forEach(bookmark -> {
            recipesIdList1.add(
                    bookmark.getRecipes().getIdx()
            );
        });

        wishStatus.forEach(wish -> {
            recipesIdList2.add(
                    wish.getRecipes().getIdx()
            );
        });
        Boolean isLiked = recipesIdList2.contains(recipes.get().getIdx());
        Boolean isBookmarked = recipesIdList1.contains(recipes.get().getIdx());


        return RecipeDetailResponse.builder()
                .recipeInfo(RecipeInfoResponseDto.builder()
                        .recipeId(recipes.get().getIdx())
                        .recipeImageUrl(recipes.get().getImage_url())
                        .recipeTitle(recipes.get().getName())
                        .dishes(recipes.get().getDish())
                        .time(recipes.get().getCookingTime())
                        .difficulty(recipes.get().getDifficulty())
                        .likeCount(recipes.get().getCountOfLikes())
                        .isBookmarked(isBookmarked)
                        .isLiked(isLiked)
                        .build()
                )
                .ingredients(ingredients_list)
                .orders(methods_list)
                .build();

    }

    //레시피 필터링 난이도 낮은 순서 && 전체조회
    @Transactional
    public List<Recipes> readAllDifficulty(){
        List<Recipes> newList = new ArrayList<>();
        newList.addAll(recipesRepository.findByDifficultyContaining("아무나"));
        newList.addAll(recipesRepository.findByDifficultyContaining("초급"));
        newList.addAll(recipesRepository.findByDifficultyContaining("중급"));

        return newList;
    }

    //레시피 필터링 난이도 낮은 순서 && 카테고리
    @Transactional
    public List<Recipes> readCategoryDifficulty(String keyword){
        List<Recipes> newList = new ArrayList<>();
        newList.addAll(recipesRepository.findByDifficultyContainingAndCategoryContaining("아무나", keyword));
        newList.addAll(recipesRepository.findByDifficultyContainingAndCategoryContaining("초급", keyword));
        newList.addAll(recipesRepository.findByDifficultyContainingAndCategoryContaining("중급", keyword));

        return newList;
    }

    //레시피 필터링 시간 낮은 순서 && 전체조회
    @Transactional
    public List<Recipes> readAllTime(){
        List<Recipes> newList = new ArrayList<>();
        newList.addAll(recipesRepository.findByCookingTimeStartsWith("5분 이내"));
        newList.addAll(recipesRepository.findByCookingTimeStartsWith("10분 이내"));
        newList.addAll(recipesRepository.findByCookingTimeStartsWith("15분 이내"));
        newList.addAll(recipesRepository.findByCookingTimeStartsWith("20분 이내"));
        newList.addAll(recipesRepository.findByCookingTimeStartsWith("30분 이내"));
        newList.addAll(recipesRepository.findByCookingTimeStartsWith("60분 이내"));
        log.info(String.valueOf(newList));
        return newList;
    }

    //레시피 필터링 시간 낮은 순서 && 카테고리
    @Transactional
    public List<Recipes> readCategoryTime(String keyword){
        List<Recipes> newList = new ArrayList<>();
        newList.addAll(recipesRepository.findByCategoryContainingAndCookingTimeStartingWith(keyword, "5분 이내"));
        newList.addAll(recipesRepository.findByCategoryContainingAndCookingTimeStartingWith(keyword, "10분 이내"));
        newList.addAll(recipesRepository.findByCategoryContainingAndCookingTimeStartingWith(keyword, "15분 이내"));
        newList.addAll(recipesRepository.findByCategoryContainingAndCookingTimeStartingWith(keyword, "20분 이내"));
        newList.addAll(recipesRepository.findByCategoryContainingAndCookingTimeStartingWith(keyword, "30분 이내"));
        newList.addAll(recipesRepository.findByCategoryContainingAndCookingTimeStartingWith(keyword, "60분 이내"));

        return newList;
    }


}
