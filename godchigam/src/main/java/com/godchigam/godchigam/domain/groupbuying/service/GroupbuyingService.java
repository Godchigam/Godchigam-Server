package com.godchigam.godchigam.domain.groupbuying.service;

import com.godchigam.godchigam.domain.groupbuying.dto.groupbuyingDto.GroupBuyingPostRequest;
import com.godchigam.godchigam.domain.groupbuying.dto.ProductInfo;
import com.godchigam.godchigam.domain.groupbuying.dto.groupbuyingDto.ProductResponse;
import com.godchigam.godchigam.domain.groupbuying.entity.JoinPeople;
import com.godchigam.godchigam.domain.groupbuying.entity.JoinStorage;
import com.godchigam.godchigam.domain.groupbuying.entity.Product;
import com.godchigam.godchigam.domain.groupbuying.entity.RequestMessage;
import com.godchigam.godchigam.domain.groupbuying.repository.*;
import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.domain.user.repository.UserRepository;
import com.godchigam.godchigam.domain.userReport.repository.UserReportRepository;
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

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GroupbuyingService {

    private final RequestRepository requestRepository;
    private final JoinStorageRepository joinStorageRepository;
    private final UserRepository userRepository;
    private final JoinPeopleRepository joinPeopleRepository;
    private final RequestMessageRepository requestMessageRepository;

    private final ProductRepository productRepository;
    private final UserReportRepository userReportRepository;

    public Product createProduct(GroupBuyingPostRequest product, String id) {
        Optional<User> user = userRepository.findByLoginId(id);

        if (!user.isPresent()) {
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        }
        Product productToCreate = new Product();
        JoinPeople peopleToCreate = new JoinPeople();
        JoinStorage storageToCreate = new JoinStorage();
        BeanUtils.copyProperties(product, productToCreate);
        BeanUtils.copyProperties(product, peopleToCreate);
        BeanUtils.copyProperties(product, storageToCreate);
        productToCreate.setWriter(user.get());
        productToCreate.setStatus("모집중");
        peopleToCreate.setJoinStatus("참여중");
        peopleToCreate.setJoinUserLoginId(id);
        peopleToCreate.setJoinStorage(storageToCreate);
        storageToCreate.setProduct(productToCreate);

        joinPeopleRepository.save(peopleToCreate);
        joinStorageRepository.save(storageToCreate);
        return productRepository.save(productToCreate);

    }

    public Product updateProduct(GroupBuyingPostRequest product, Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new BaseException(ErrorCode.EMPTY_PRODUCT_ID);
        }
        Product product1 = optionalProduct.get();
        product1.setProductImageUrl(product.getProductImageUrl());
        product1.setProductName(product.getProductName());
        product1.setProductPrice(product.getProductPrice());
        product1.setCategory(product.getCategory());
        product1.setGoalPeopleCount(product.getGoalPeopleCount());
        product1.setDealingMethod(product.getDealingMethod());
        product1.setDescription(product.getDescription());
        productRepository.save(product1);
        return null;
    }


    public Product deleteProduct(Long id, String userId) {

        Optional<User> user = userRepository.findByLoginId(userId);

        if (!user.isPresent()) {
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        } else {

            //List 에서 Optional로 바꿈
            Optional<JoinStorage> optionalJoinStorage = joinStorageRepository.findByProduct(id);
            Long storageId = null;
            if (!optionalJoinStorage.isEmpty()) {
                storageId = optionalJoinStorage.get().getJoinStorageIdx();
            }

            List<JoinPeople> optionalJoinPeople = joinPeopleRepository.findByJoinStorage(storageId);
            if (!optionalJoinPeople.isEmpty()) {
                optionalJoinPeople.get(0).getJoinerIdx();
            }

            joinPeopleRepository.deleteAll(joinPeopleRepository.findByJoinStorage(storageId));
            joinStorageRepository.delete(optionalJoinStorage.get());
            //추가코드
            requestMessageRepository.delete(requestMessageRepository.findByProduct(id));
            productRepository.deleteById(id);


            return null;

        }
    }


    public ProductResponse groupBuyingMain(String loginId) {
        Optional<User> user = userRepository.findByLoginId(loginId);
        Long userIdx = user.get().getUserIdx();
        log.info(String.valueOf(userIdx));
        Integer reportCnt = userReportRepository.findByReportId(userIdx).size();
        log.info(String.valueOf(reportCnt));
        //글 아이디:
        String userAddress = user.get().getAddress();
        List<Product> loginUserProductList = productRepository.findByWriter1(userAddress);
        List<ProductInfo> food = new ArrayList<>();
        List<ProductInfo> living = new ArrayList<>();
        List<ProductInfo> etc = new ArrayList<>();
        if (!user.isPresent()) {
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        } else {
            loginUserProductList.forEach(product -> {
                Long productId = product.getProductIdx();

                //List 에서 Optional로 바꿈
                Optional<JoinStorage> optionalJoinStorage = joinStorageRepository.findByProduct(productId);
                Long storageId = null;
                Integer cntOfPeople = 0;
                if (!optionalJoinStorage.isEmpty()) {
                    storageId = optionalJoinStorage.get().getJoinStorageIdx();
                }

                List<JoinPeople> optionalJoinPeople = joinPeopleRepository.findByJoinStorage(storageId);
                List<JoinPeople> test = new ArrayList<>();
                if (!optionalJoinPeople.isEmpty()) {
                    cntOfPeople = optionalJoinPeople.size();

                    optionalJoinPeople.forEach(joinPeople -> {
                        if(joinPeople.getJoinStatus().equals("참여중")||joinPeople.getJoinStatus().equals("탈퇴대기")){
                            test.add(joinPeople);

                        }

                    });
                    if (product.getCategory().equals("식품")) {
                        food.add(
                                ProductInfo.builder()
                                        .productId(product.getProductIdx())
                                        .productImageUrl(product.getProductImageUrl())
                                        .purchaseStatus(product.getStatus())
                                        .productName(product.getProductName())
                                        .productPrice(product.getProductPrice())
                                        .dividedPrice(product.getProductPrice() / product.getGoalPeopleCount())
                                        .goalPeopleCount(product.getGoalPeopleCount())
                                        .joinPeopleCount(test.size())
                                        .dealingMethod(product.getDealingMethod())
                                        .build()
                        );

                    } else if (product.getCategory().equals("생필품")) {
                        living.add(
                                ProductInfo.builder()
                                        .productId(product.getProductIdx())
                                        .productImageUrl(product.getProductImageUrl())
                                        .purchaseStatus(product.getStatus())
                                        .productName(product.getProductName())
                                        .productPrice(product.getProductPrice())
                                        .dividedPrice(product.getProductPrice() / product.getGoalPeopleCount())
                                        .goalPeopleCount(product.getGoalPeopleCount())
                                        .joinPeopleCount(test.size())
                                        .dealingMethod(product.getDealingMethod())
                                        .build()
                        );
                    } else if (product.getCategory().equals("그 외")) {
                        etc.add(
                                ProductInfo.builder()
                                        .productId(product.getProductIdx())
                                        .productImageUrl(product.getProductImageUrl())
                                        .purchaseStatus(product.getStatus())
                                        .productName(product.getProductName())
                                        .productPrice(product.getProductPrice())
                                        .dividedPrice(product.getProductPrice() / product.getGoalPeopleCount())
                                        .goalPeopleCount(product.getGoalPeopleCount())
                                        .joinPeopleCount(test.size())
                                        .dealingMethod(product.getDealingMethod())
                                        .build()
                        );

                    }
                }


            });

            return ProductResponse.builder()
                    .reportCount(reportCnt)
                    .address(userAddress)
                    .food(food)
                    .living(living)
                    .etc(etc)
                    .build();
        }
    }

    public List<ProductInfo> myGroupBuying(String loginId, String type) {
        Optional<User> user = userRepository.findByLoginId(loginId);
        List<ProductInfo> newList = new ArrayList<>();
        List<Product> loginUserProductList = null;
        List<JoinPeople> loginUserJoinPeopleList = null;

        if (type.equals("register")) {
            loginUserProductList = productRepository.findByWriter(loginId);
            if (!user.isPresent()) {
                throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
            }

            loginUserProductList.forEach(product -> {
                Long productId = product.getProductIdx();
                //List 에서 Optional로 바꿈
                Optional<JoinStorage> optionalJoinStorage = joinStorageRepository.findByProduct(productId);
                Long storageId = null;
                Integer cntOfPeople = null;
                if (!optionalJoinStorage.isEmpty()) {
                    storageId = optionalJoinStorage.get().getJoinStorageIdx();
                }

                List<JoinPeople> optionalJoinPeople = joinPeopleRepository.findByJoinStorage(storageId);
                List<JoinPeople> test2 = new ArrayList<>();
                if (!optionalJoinPeople.isEmpty()) {

                    optionalJoinPeople.forEach(joinPeople -> {
                        if (joinPeople.getJoinStatus().equals("참여중") || joinPeople.getJoinStatus().equals("탈퇴대기")) {
                            test2.add(joinPeople);

                        }

                    });
                    newList.add(ProductInfo.builder()
                            .productId(product.getProductIdx())
                            .productImageUrl(product.getProductImageUrl())
                            .purchaseStatus(product.getStatus())
                            .productName(product.getProductName())
                            .productPrice(product.getProductPrice())
                            .dividedPrice(product.getProductPrice() / product.getGoalPeopleCount())
                            .goalPeopleCount(product.getGoalPeopleCount())
                            .joinPeopleCount(test2.size())
                            .dealingMethod(product.getDealingMethod())
                            .build());



                }


            });


        } else if (type.equals("join")) {
            if (!user.isPresent()) {
                throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
            }
            loginUserJoinPeopleList = joinPeopleRepository.findByJoinStatusAndJoinUserLoginId(loginId);
            loginUserJoinPeopleList.forEach(people -> {

                        Long storageId1 = null;
                        Integer cntOfPeople1 = null;
                        storageId1 = people.getJoinStorage().getJoinStorageIdx();
                        List<JoinPeople> optionalJoinPeople = joinPeopleRepository.findByJoinStorage(storageId1);
                        if (!optionalJoinPeople.isEmpty()) {

                            cntOfPeople1 = optionalJoinPeople.size();
                        }
                        if (!people.getJoinStorage().getProduct().getWriter().getLoginId().equals(loginId)) {

                            newList.add(ProductInfo.builder()
                                    .productId(people.getJoinStorage().getProduct().getProductIdx())
                                    .productImageUrl(people.getJoinStorage().getProduct().getProductImageUrl())
                                    .purchaseStatus(people.getJoinStorage().getProduct().getStatus())
                                    .productName(people.getJoinStorage().getProduct().getProductName())
                                    .productPrice(people.getJoinStorage().getProduct().getProductPrice())
                                    .dividedPrice(people.getJoinStorage().getProduct().getProductPrice() / people.getJoinStorage().getProduct().getGoalPeopleCount())
                                    .goalPeopleCount(people.getJoinStorage().getProduct().getGoalPeopleCount())
                                    .joinPeopleCount(cntOfPeople1)
                                    .dealingMethod(people.getJoinStorage().getProduct().getDealingMethod())
                                    .build());

                        }
                    }
            );
        }

        return newList;

    }
}