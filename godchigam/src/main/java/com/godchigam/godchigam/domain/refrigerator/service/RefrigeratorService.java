package com.godchigam.godchigam.domain.refrigerator.service;


import com.godchigam.godchigam.domain.refrigerator.dto.FoodInfoResponse;
import com.godchigam.godchigam.domain.refrigerator.dto.RefrigeratorResponse;
import com.godchigam.godchigam.domain.refrigerator.entity.Ingredient;
import com.godchigam.godchigam.domain.refrigerator.entity.Refrigerator;
import com.godchigam.godchigam.domain.refrigerator.repository.IngredientRepository;
import com.godchigam.godchigam.domain.refrigerator.repository.RefrigeratorRepository;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.dto.DateInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RefrigeratorService {

    private final RefrigeratorRepository refrigeratorRepository;
    private final IngredientRepository ingredientRepository;

    public RefrigeratorResponse LookUpRefrigerator(String loginId) {

        Optional<Refrigerator> loginUserFrige = refrigeratorRepository.findByUser(loginId);
        Long loginUserFrigeIdx = loginUserFrige.get().getFrigeIdx();

        List<Ingredient> loginUserIngredientList = refrigeratorRepository.findByFrigeIdx(loginUserFrigeIdx);
        List<FoodInfoResponse> freezer = new ArrayList<>();
        List<FoodInfoResponse> cold = new ArrayList<>();
        List<FoodInfoResponse> room = new ArrayList<>();

        /**
         * 날짜 저장 관련 다시 조사
         */
        loginUserIngredientList.forEach(ingredient -> {
            if (ingredient.getIngredientStatus().equals("freezer")) {
                freezer.add(
                        FoodInfoResponse.builder()
                                .foodId(ingredient.getIngredientIdx())
                                .foodName(ingredient.getIngredientName())
                                .expirationDate(DateInfoResponse.builder()
                                        .year(ingredient.getIngredientLimit().getYear())
                                        .month(ingredient.getIngredientLimit().getMonth())
                                        .day(ingredient.getIngredientLimit().getDay())
                                        .build())
                                .purchaseDate(DateInfoResponse.builder()
                                        .year(ingredient.getIngredientBuy().getYear())
                                        .month(ingredient.getIngredientBuy().getMonth())
                                        .day(ingredient.getIngredientBuy().getDay())
                                        .build())
                                .amount(ingredient.getIngredientCnt())
                                .remain(65)
                                .storage(ingredient.getIngredientStatus())
                                .build()
                );
            } else if (ingredient.getIngredientStatus().equals("cold")) {
                cold.add(
                        FoodInfoResponse.builder()
                                .foodId(ingredient.getIngredientIdx())
                                .foodName(ingredient.getIngredientName())
                                .expirationDate(DateInfoResponse.builder()
                                        .year(ingredient.getIngredientLimit().getYear())
                                        .month(ingredient.getIngredientLimit().getMonth())
                                        .day(ingredient.getIngredientLimit().getDay())
                                        .build())
                                .purchaseDate(DateInfoResponse.builder()
                                        .year(ingredient.getIngredientBuy().getYear())
                                        .month(ingredient.getIngredientBuy().getMonth())
                                        .day(ingredient.getIngredientBuy().getDay())
                                        .build())
                                .amount(ingredient.getIngredientCnt())
                                .remain(65)
                                .storage(ingredient.getIngredientStatus())
                                .build()
                );
            } else if (ingredient.getIngredientStatus().equals("room")) {
                room.add(
                        FoodInfoResponse.builder()
                                .foodId(ingredient.getIngredientIdx())
                                .foodName(ingredient.getIngredientName())
                                .expirationDate(DateInfoResponse.builder()
                                        .year(ingredient.getIngredientLimit().getYear())
                                        .month(ingredient.getIngredientLimit().getMonth())
                                        .day(ingredient.getIngredientLimit().getDay())
                                        .build())
                                .purchaseDate(DateInfoResponse.builder()
                                        .year(ingredient.getIngredientBuy().getYear())
                                        .month(ingredient.getIngredientBuy().getMonth())
                                        .day(ingredient.getIngredientBuy().getDay())
                                        .build())
                                .amount(ingredient.getIngredientCnt())
                                .remain(65)
                                .storage(ingredient.getIngredientStatus())
                                .build()
                );
            }
        });

        return RefrigeratorResponse.builder()
                .freezer(freezer)
                .cold(cold)
                .room(room)
                .build();
    }

    public FoodInfoResponse LookUpDetailIngredientInfo(Long foodId){
        Optional<Ingredient> selectedIngredient = ingredientRepository.findByIngredientIdx(foodId);
        return FoodInfoResponse.builder()
                .foodId(selectedIngredient.get().getIngredientIdx())
                .foodName(selectedIngredient.get().getIngredientName())
                .expirationDate(DateInfoResponse.builder()
                        .year(selectedIngredient.get().getIngredientLimit().getYear())
                        .month(selectedIngredient.get().getIngredientLimit().getMonth())
                        .day(selectedIngredient.get().getIngredientLimit().getDay())
                        .build())
                .purchaseDate(DateInfoResponse.builder()
                        .year(selectedIngredient.get().getIngredientBuy().getYear())
                        .month(selectedIngredient.get().getIngredientBuy().getMonth())
                        .day(selectedIngredient.get().getIngredientBuy().getDay())
                        .build())
                .amount(selectedIngredient.get().getIngredientCnt())
                .remain(65)
                .storage(selectedIngredient.get().getIngredientStatus())
                .build();
    }
}
