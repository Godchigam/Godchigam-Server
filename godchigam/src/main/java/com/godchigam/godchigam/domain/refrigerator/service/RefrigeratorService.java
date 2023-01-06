package com.godchigam.godchigam.domain.refrigerator.service;


import com.godchigam.godchigam.domain.refrigerator.dto.AddIngredientResponse;
import com.godchigam.godchigam.domain.refrigerator.dto.FoodInfoResponse;
import com.godchigam.godchigam.domain.refrigerator.dto.NewIngredientRequest;
import com.godchigam.godchigam.domain.refrigerator.dto.RefrigeratorResponse;
import com.godchigam.godchigam.domain.refrigerator.entity.Ingredient;
import com.godchigam.godchigam.domain.refrigerator.entity.Refrigerator;
import com.godchigam.godchigam.domain.refrigerator.repository.IngredientRepository;
import com.godchigam.godchigam.domain.refrigerator.repository.RefrigeratorRepository;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import com.godchigam.godchigam.global.dto.DateInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

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

        loginUserIngredientList.forEach(ingredient -> {
            LocalDate currentTime = LocalDate.now();
            Period diff = Period.between(currentTime, ingredient.getIngredientLimitDate());
            int diff_time = diff.getYears() * 365 + diff.getMonths() * 30 + diff.getDays();
            if (ingredient.getIngredientStatus().equals("freezer")) {
                freezer.add(
                        FoodInfoResponse.builder()
                                .foodId(ingredient.getIngredientIdx())
                                .foodName(ingredient.getIngredientName())
                                .expirationDate(DateInfoResponse.builder()
                                        .year(ingredient.getIngredientLimitDate().getYear())
                                        .month(ingredient.getIngredientLimitDate().getMonthValue())
                                        .day(Integer.parseInt(ingredient.getIngredientLimitDate().toString().substring(8)))
                                        .build())
                                .purchaseDate(DateInfoResponse.builder()
                                        .year(ingredient.getIngredientBuyDate().getYear())
                                        .month(ingredient.getIngredientBuyDate().getMonthValue())
                                        .day(Integer.parseInt(ingredient.getIngredientBuyDate().toString().substring(8)))
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
                                        .year(ingredient.getIngredientLimitDate().getYear())
                                        .month(ingredient.getIngredientLimitDate().getMonthValue())
                                        .day(Integer.parseInt(ingredient.getIngredientLimitDate().toString().substring(8)))
                                        .build())
                                .purchaseDate(DateInfoResponse.builder()
                                        .year(ingredient.getIngredientBuyDate().getYear())
                                        .month(ingredient.getIngredientBuyDate().getMonthValue())
                                        .day(Integer.parseInt(ingredient.getIngredientBuyDate().toString().substring(8)))
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
                                        .year(ingredient.getIngredientLimitDate().getYear())
                                        .month(ingredient.getIngredientLimitDate().getMonthValue())
                                        .day(Integer.parseInt(ingredient.getIngredientLimitDate().toString().substring(8)))
                                        .build())
                                .purchaseDate(DateInfoResponse.builder()
                                        .year(ingredient.getIngredientBuyDate().getYear())
                                        .month(ingredient.getIngredientBuyDate().getMonthValue())
                                        .day(Integer.parseInt(ingredient.getIngredientBuyDate().toString().substring(8)))
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

        if (selectedIngredient.isEmpty()) {
            throw new BaseException(ErrorCode.EMPTY_FOOD_ID);
        }

        LocalDate currentTime = LocalDate.now();
        Period diff = Period.between(currentTime, selectedIngredient.get().getIngredientLimitDate());
        int diff_time = diff.getYears() * 365 + diff.getMonths() * 30 + diff.getDays();
        log.info("날짜 차이:" + diff.getYears() + diff.getMonths() + diff.getDays());
        return FoodInfoResponse.builder()
                .foodId(selectedIngredient.get().getIngredientIdx())
                .foodName(selectedIngredient.get().getIngredientName())
                .expirationDate(DateInfoResponse.builder()
                        .year(selectedIngredient.get().getIngredientLimitDate().getYear())
                        .month(selectedIngredient.get().getIngredientLimitDate().getMonthValue())
                        .day(Integer.parseInt(selectedIngredient.get().getIngredientLimitDate().toString().substring(8)))
                        .build())
                .purchaseDate(DateInfoResponse.builder()
                        .year(selectedIngredient.get().getIngredientBuyDate().getYear())
                        .month(selectedIngredient.get().getIngredientBuyDate().getMonthValue())
                        .day(Integer.parseInt(selectedIngredient.get().getIngredientBuyDate().toString().substring(8)))
                        .build())
                .amount(selectedIngredient.get().getIngredientCnt())
                .remain(diff_time)
                .storage(selectedIngredient.get().getIngredientStatus())
                .build();
    }

    public AddIngredientResponse addNewIngredient(String loginId, NewIngredientRequest newIngredientRequest) {
        Optional<Refrigerator> loginUserFrige = refrigeratorRepository.findByUser(loginId);

        Ingredient newIngredient = new Ingredient();
        newIngredient.setIngredientName(newIngredientRequest.getFoodName());
        newIngredient.setIngredientCnt(newIngredientRequest.getAmount());
        newIngredient.setIngredientStatus(newIngredientRequest.getStorage());

        LocalDate limit = LocalDate.of(newIngredientRequest.getExpirationYear(), newIngredientRequest.getExpirationMonth(), newIngredientRequest.getExpirationDay());
        LocalDate purchase = LocalDate.of(newIngredientRequest.getPurchaseYear(), newIngredientRequest.getPurchaseMonth(), newIngredientRequest.getPurchaseDay());
        newIngredient.setIngredientBuyDate(purchase);
        newIngredient.setIngredientLimitDate(limit);
        newIngredient.setRefrigerator(loginUserFrige.get());

        ingredientRepository.save(newIngredient);

        return AddIngredientResponse.builder().foodId(newIngredient.getIngredientIdx()).build();
    }

    public int addIngredientAmount(int changeAmount, Long foodId) {
        Optional<Ingredient> selectIngredient = ingredientRepository.findByIngredientIdx(foodId);

        if (selectIngredient.isEmpty()) {
            throw new BaseException(ErrorCode.EMPTY_FOOD_ID);
        }

        int curAmount = selectIngredient.get().getIngredientCnt();
        int newAmonut = curAmount + changeAmount;
        if (newAmonut <= 0) {
            throw new BaseException(ErrorCode.TOO_SMALL_AMOUNT);
        } else if (newAmonut > 99) {
            throw new BaseException(ErrorCode.TOO_MANY_AMOUNT);
        }
        selectIngredient.get().setIngredientCnt(newAmonut);
        return newAmonut;
    }

    public void deleteIngredient(Long foodId) {
        Optional<Ingredient> selectIngredient = ingredientRepository.findByIngredientIdx(foodId);
        if (selectIngredient.isEmpty()) {
            throw new BaseException(ErrorCode.EMPTY_FOOD_ID);
        }
        ingredientRepository.deleteById(foodId);
    }

    public void changeIngredientDetail(NewIngredientRequest changeIngredientRequest, Long foodId) {
        Optional<Ingredient> selectIngredient = ingredientRepository.findByIngredientIdx(foodId);

        if (selectIngredient.isEmpty()) {
            throw new BaseException(ErrorCode.EMPTY_FOOD_ID);
        }

        Ingredient changeIngredient = selectIngredient.get();
        changeIngredient.setIngredientCnt(changeIngredientRequest.getAmount());
        changeIngredient.setIngredientName(changeIngredientRequest.getFoodName());
        log.info("바뀐이름" + changeIngredient.getIngredientName());
        LocalDate limit = LocalDate.of(changeIngredientRequest.getExpirationYear(), changeIngredientRequest.getExpirationMonth(), changeIngredientRequest.getExpirationDay());
        LocalDate purchase = LocalDate.of(changeIngredientRequest.getPurchaseYear(), changeIngredientRequest.getPurchaseMonth(), changeIngredientRequest.getPurchaseDay());
        changeIngredient.setIngredientLimitDate(limit);
        changeIngredient.setIngredientBuyDate(purchase);

        changeIngredient.setIngredientStatus(changeIngredientRequest.getStorage());

    }
}
