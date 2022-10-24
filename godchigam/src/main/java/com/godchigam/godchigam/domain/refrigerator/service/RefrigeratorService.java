package com.godchigam.godchigam.domain.refrigerator.service;


import com.godchigam.godchigam.domain.refrigerator.dto.FoodInfoResponse;
import com.godchigam.godchigam.domain.refrigerator.dto.NewIngredientRequest;
import com.godchigam.godchigam.domain.refrigerator.dto.RefrigeratorResponse;
import com.godchigam.godchigam.domain.refrigerator.entity.Ingredient;
import com.godchigam.godchigam.domain.refrigerator.entity.Refrigerator;
import com.godchigam.godchigam.domain.refrigerator.repository.IngredientRepository;
import com.godchigam.godchigam.domain.refrigerator.repository.RefrigeratorRepository;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.dto.DateInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
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
            LocalDate currentTime= LocalDate.now();
            Period diff = Period.between(currentTime,ingredient.getIngredientLimit());
            int diff_time = diff.getYears()*365 + diff.getMonths()*30 + diff.getDays();
            if (ingredient.getIngredientStatus().equals("freezer")) {
                freezer.add(
                        FoodInfoResponse.builder()
                                .foodId(ingredient.getIngredientIdx())
                                .foodName(ingredient.getIngredientName())
                                .expirationDate(DateInfoResponse.builder()
                                        .year(ingredient.getIngredientLimit().getYear())
                                        .month(ingredient.getIngredientLimit().getMonthValue())
                                        .day(Integer.parseInt(ingredient.getIngredientLimit().toString().substring(8)))
                                        .build())
                                .purchaseDate(DateInfoResponse.builder()
                                        .year(ingredient.getIngredientBuy().getYear())
                                        .month(ingredient.getIngredientBuy().getMonthValue())
                                        .day(Integer.parseInt(ingredient.getIngredientBuy().toString().substring(8)))
                                        .build())
                                .amount(ingredient.getIngredientCnt())
                                .remain(diff_time)
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
                                        .month(ingredient.getIngredientLimit().getDayOfMonth())
                                        .day(Integer.parseInt(ingredient.getIngredientLimit().toString().substring(8)))
                                        .build())
                                .purchaseDate(DateInfoResponse.builder()
                                        .year(ingredient.getIngredientBuy().getYear())
                                        .month(ingredient.getIngredientBuy().getDayOfMonth())
                                        .day(Integer.parseInt(ingredient.getIngredientBuy().toString().substring(8)))
                                        .build())
                                .amount(ingredient.getIngredientCnt())
                                .remain(diff_time)
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
                                        .month(ingredient.getIngredientLimit().getDayOfMonth())
                                        .day(Integer.parseInt(ingredient.getIngredientLimit().toString().substring(8)))
                                        .build())
                                .purchaseDate(DateInfoResponse.builder()
                                        .year(ingredient.getIngredientBuy().getYear())
                                        .month(ingredient.getIngredientBuy().getDayOfMonth())
                                        .day(Integer.parseInt(ingredient.getIngredientBuy().toString().substring(8)))
                                        .build())
                                .amount(ingredient.getIngredientCnt())
                                .remain(diff_time)
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

    public FoodInfoResponse LookUpDetailIngredientInfo(Long foodId) {
        Optional<Ingredient> selectedIngredient = ingredientRepository.findByIngredientIdx(foodId);

        LocalDate currentTime= LocalDate.now();
        Period diff = Period.between(currentTime,selectedIngredient.get().getIngredientLimit());
        int diff_time = diff.getYears()*365 + diff.getMonths()*30 + diff.getDays();
        log.info("날짜 차이:"+diff.getYears()+diff.getMonths()+diff.getDays());
        return FoodInfoResponse.builder()
                .foodId(selectedIngredient.get().getIngredientIdx())
                .foodName(selectedIngredient.get().getIngredientName())
                .expirationDate(DateInfoResponse.builder()
                        .year(selectedIngredient.get().getIngredientLimit().getYear())
                        .month(selectedIngredient.get().getIngredientLimit().getMonthValue())
                        .day(Integer.parseInt(selectedIngredient.get().getIngredientLimit().toString().substring(8)))
                        .build())
                .purchaseDate(DateInfoResponse.builder()
                        .year(selectedIngredient.get().getIngredientBuy().getYear())
                        .month(selectedIngredient.get().getIngredientBuy().getMonthValue())
                        .day(Integer.parseInt(selectedIngredient.get().getIngredientBuy().toString().substring(8)))
                        .build())
                .amount(selectedIngredient.get().getIngredientCnt())
                .remain(diff_time)
                .storage(selectedIngredient.get().getIngredientStatus())
                .build();
    }

    public void addNewIngredient(String loginId,NewIngredientRequest newIngredientRequest) {
        Optional<Refrigerator> loginUserFrige = refrigeratorRepository.findByUser(loginId);

        Ingredient newIngredient = new Ingredient();
        newIngredient.setIngredientName(newIngredientRequest.getFoodName());
        newIngredient.setIngredientCnt(newIngredientRequest.getAmount());
        newIngredient.setIngredientStatus(newIngredientRequest.getStorage());

        LocalDate limit = LocalDate.of(newIngredientRequest.getExpirationYear(),newIngredientRequest.getExpirationMonth(),newIngredientRequest.getExpirationDay());
        LocalDate purchase = LocalDate.of(newIngredientRequest.getPurchaseYear(),newIngredientRequest.getPurchaseMonth(),newIngredientRequest.getPurchaseDay());
        newIngredient.setIngredientBuy(purchase);
        newIngredient.setIngredientLimit(limit);
        newIngredient.setRefrigerator(loginUserFrige.get());

        ingredientRepository.save(newIngredient);
    }
}
