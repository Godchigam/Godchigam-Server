package com.godchigam.godchigam.domain.groupbuying.service;

import com.godchigam.godchigam.domain.groupbuying.dto.*;
import com.godchigam.godchigam.domain.groupbuying.entity.*;
import com.godchigam.godchigam.domain.groupbuying.repository.JoinPeopleRepository;
import com.godchigam.godchigam.domain.groupbuying.repository.JoinStorageRepository;
import com.godchigam.godchigam.domain.groupbuying.repository.ProductRepository;
import com.godchigam.godchigam.domain.groupbuying.repository.RequestRepository;
import com.godchigam.godchigam.domain.recipesBookmark.dto.RecipeInfoResponseDto;
import com.godchigam.godchigam.domain.refrigerator.dto.FoodInfoResponse;
import com.godchigam.godchigam.domain.refrigerator.dto.RefrigeratorResponse;
import com.godchigam.godchigam.domain.refrigerator.entity.Ingredient;
import com.godchigam.godchigam.domain.refrigerator.entity.Refrigerator;
import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.domain.user.repository.UserRepository;
import com.godchigam.godchigam.domain.userReport.repository.UserReportRepository;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Join;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RequestService {

    private final RequestRepository requestRepository;
    private final JoinStorageRepository joinStorageRepository;
    private final UserRepository userRepository;
    private final JoinPeopleRepository joinPeopleRepository;
    private final ProductRepository productRepository;
    private final UserReportRepository userReportRepository;
/*
    public List<RequestMessageResponse> LookUpRequestStorage(String loginId) {

        Optional<RequestStorage> loginUserStorage = requestRepository.findByUser(loginId);
        Long loginUserStorageIdx = loginUserStorage.get().getRequestStorageIdx();

        List<RequestMessage> loginUserRequestMessageList = requestRepository.findByRequestStorageIdx(loginUserStorageIdx);
        List<RequestMessageResponse> resultMessageList = new ArrayList<>();

        loginUserRequestMessageList.forEach(requestMessage -> {

            UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                    .userId(requestMessage.getRequestSendUser().getUserIdx())
                    .profileImageUrl(requestMessage.getRequestSendUser().getProfileImageUrl())
                    .nickname(requestMessage.getRequestSendUser().getProfileImageUrl())
                    .build();
            resultMessageList.add(
                    RequestMessageResponse.builder()
                            .requesterInfo(userInfoResponse)
                            .requestId(requestMessage.getRequestIdx())
                            .productName(requestMessage.getRequestProduct().getProductName())
                            .productId(requestMessage.getRequestProduct().getProductIdx())
                            .requestType(requestMessage.getRequestType())
                            .build()
            );
        });

        if (resultMessageList.isEmpty()) return null;
        return resultMessageList;
    }

    public UserJoinStatusResponse checkProductJoinStatus(String loginId, Long productId){

        Optional<JoinStorage> joinStorage = joinStorageRepository.findByProduct(productId);
        if(joinStorage.isEmpty()){
            throw new BaseException(ErrorCode.EMPTY_PRODUCT_ID);
        }

        Long joinStorageId = joinStorage.get().getJoinStorageIdx();
        List<JoinPeople> joinPeopleList = joinStorageRepository.findByJoinStorageIdx(joinStorageId);

        UserJoinStatusResponse resultResponse= UserJoinStatusResponse.builder().joinType("참여안함").build();

        joinPeopleList.forEach(joinPeople -> {
            if(joinPeople.getJoinUserLoginId().equals(loginId)){
                resultResponse.setJoinType(joinPeople.getJoinStatus());
            }
        });

        return resultResponse;
    }

    public List<UserInfoResponse> checkProductJoinPeople(String loginId, Long productId) {

        Optional<JoinStorage> joinStorage = joinStorageRepository.findByProduct(productId);
        if (joinStorage.isEmpty()) {
            throw new BaseException(ErrorCode.EMPTY_PRODUCT_ID);
        }

        Long joinStorageId = joinStorage.get().getJoinStorageIdx();
        List<JoinPeople> joinPeopleList = joinStorageRepository.findByJoinStorageIdx(joinStorageId);

        List<UserInfoResponse> resultList = new ArrayList<>();

        joinPeopleList.forEach(joinPeople -> {
            if (!(joinPeople.getJoinUserLoginId().equals(loginId)) && joinPeople.getJoinStatus().equals("참여중")) {

                Optional<User> user = userRepository.findByLoginId(joinPeople.getJoinUserLoginId());
                resultList.add(UserInfoResponse.builder()
                        .userId(user.get().getUserIdx())
                        .profileImageUrl(user.get().getProfileImageUrl())
                        .nickname(user.get().getNickname())
                        .build()
                );
            }
        });

        return resultList;
    }

    public DetailProductInfoResponse detailProductInfo(String loginId, Long productId) {
        Optional<JoinStorage> joinStorage = joinStorageRepository.findByProduct(productId);
        if (joinStorage.isEmpty()) {
            throw new BaseException(ErrorCode.EMPTY_PRODUCT_ID);
        }

        Product product = joinStorage.get().getProduct();
        User writer = product.getWriter();
        Boolean isOwner = false;
        if (writer.getLoginId().equals(loginId)) {
            isOwner = true;
        }

        Boolean isFull = true;
        if (product.getStatus().equals("모집중")) {
            isFull = false;
        }

        Long joinStorageId = joinStorage.get().getJoinStorageIdx();
        List<JoinPeople> joinPeopleList = joinStorageRepository.findByJoinStorageIdx(joinStorageId);

        List<UserInfoResponse> joinnerList = new ArrayList<>(); //실제 참여중인 애들만 뽑기
        List<String> checkMyStatus = new ArrayList<>();

        joinPeopleList.forEach(joinPeople -> {
            if (joinPeople.getJoinUserLoginId().equals(loginId)) {
                checkMyStatus.add(joinPeople.getJoinStatus());
            }
            if (joinPeople.getJoinStatus().equals("참여중")) {
                Optional<User> addUser = userRepository.findByLoginId(joinPeople.getJoinUserLoginId());
                joinnerList.add(UserInfoResponse.builder()
                        .userId(addUser.get().getUserIdx())
                        .nickname(addUser.get().getNickname())
                        .profileImageUrl(addUser.get().getProfileImageUrl())
                        .build());
            }
            ;
        });

        String myJoinType = "";
        if (checkMyStatus.isEmpty()) {
            myJoinType = "참여안함";
        } else {
            myJoinType = checkMyStatus.get(0);
        }

        ProductInfo productInfo = ProductInfo.builder()
                .productId(product.getProductIdx())
                .productImageUrl(product.getProductImageUrl())
                .purchaseStatus(product.getStatus())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .dividedPrice(product.getProductPrice() / product.getGoalPeopleCount())
                .goalPeopleCount(product.getGoalPeopleCount())
                .joinPeopleCount(joinnerList.size())
                .dealingMethod(product.getDealingMethod())
                .build();

        return DetailProductInfoResponse.builder()
                .ifOwner(isOwner)
                .isFull(isFull)
                .joinType(myJoinType)
                .address(writer.getAddress())
                .category(product.getCategory())
                .productInfo(productInfo)
                .owner(UserInfoResponse.builder()
                        .userId(writer.getUserIdx())
                        .nickname(writer.getNickname())
                        .profileImageUrl(writer.getProfileImageUrl())
                        .build())
                .joinPeople(joinnerList)
                .description(product.getDescription())
                .build();

    }

    public ChangeProductStatus changeProductStatus(String loginId, Long productId) {

        Optional<User> writer = userRepository.findByLoginId(loginId);
        if (writer.isEmpty()) {
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        }

        Optional<JoinStorage> joinStorage = joinStorageRepository.findByProduct(productId);
        if (joinStorage.isEmpty()) {
            throw new BaseException(ErrorCode.EMPTY_PRODUCT_ID);
        }

        ChangeProductStatus changeProductStatus = new ChangeProductStatus();
        Product product = joinStorage.get().getProduct();
        if (product.getStatus().equals("종료")) {

            Long joinStorageId = joinStorage.get().getJoinStorageIdx();
            List<JoinPeople> joinPeopleList = joinStorageRepository.findByJoinStorageIdx(joinStorageId);

            List<UserInfoResponse> joinnerList = new ArrayList<>(); //실제 참여중인 애들만 뽑기
            joinPeopleList.forEach(joinPeople -> {
                if (joinPeople.getJoinStatus().equals("참여중")) {
                    Optional<User> addUser = userRepository.findByLoginId(joinPeople.getJoinUserLoginId());
                    joinnerList.add(UserInfoResponse.builder()
                            .userId(addUser.get().getUserIdx())
                            .nickname(addUser.get().getNickname())
                            .profileImageUrl(addUser.get().getProfileImageUrl())
                            .build());
                }
            });

            if(product.getGoalPeopleCount().equals(joinnerList.size())) {
                changeProductStatus.setPurchaseStatus("모집완료");
            } else {
                changeProductStatus.setPurchaseStatus("모집중");
            }

        } else {
            changeProductStatus.setPurchaseStatus("종료");
       }
        return changeProductStatus;
    }

    public JoinStatusResponse sendJoinRequest(String loginId, Long productId) {

        Optional<User> writer = userRepository.findByLoginId(loginId);
        if (writer.isEmpty()) {
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        }

        Optional<JoinStorage> joinStorage = joinStorageRepository.findByProduct(productId);
        if (joinStorage.isEmpty()) {
            throw new BaseException(ErrorCode.EMPTY_PRODUCT_ID);
        }

        Long joinStorageId = joinStorage.get().getJoinStorageIdx();
        List<JoinPeople> joinPeopleList = joinStorageRepository.findByJoinStorageIdx(joinStorageId);

        List<JoinPeople> checkMyStatus = new ArrayList<>();

        joinPeopleList.forEach(joinPeople -> {
            if (joinPeople.getJoinUserLoginId().equals(loginId)) {
                checkMyStatus.add(joinPeople);
            }
        });

        String myJoinType = "";
        if (checkMyStatus.isEmpty()) {
            myJoinType = "참여대기";
            JoinPeople newJoinner = new JoinPeople();
            newJoinner.setJoinUserLoginId(loginId);
            newJoinner.setJoinStatus("참여대기");
            newJoinner.setJoinStorage(joinStorage.get());

            joinPeopleRepository.save(newJoinner);
        } else {
            JoinPeople currentUser = checkMyStatus.get(0);
            myJoinType = currentUser.getJoinStatus(); //참여중 일수도 있고 참여안함 일수도 있음

            if (myJoinType.equals("참여중")) {
                currentUser.setJoinStatus("탈퇴대기");
                myJoinType = "탈퇴대기";
            } else if (myJoinType.equals("참여안함")) {
                currentUser.setJoinStatus("참여대기");
                myJoinType = "참여대기";
            }

            joinPeopleRepository.save(currentUser);
        }

        JoinStatusResponse joinStatusResponse= new JoinStatusResponse(myJoinType);
        return joinStatusResponse;
    }


 */

    //글 등록
    public Product createProduct(GroupBuyingPostRequest product, String id){
        Optional<User> user = userRepository.findByLoginId(id);

        if(!user.isPresent()){
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

    //글 수정
    public Product updateProduct(GroupBuyingPostRequest product, Long id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(!optionalProduct.isPresent()){
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


    //글 삭제
    public Product deleteProduct(Long id, String userId){

        Optional<User> user = userRepository.findByLoginId(userId);

        if(!user.isPresent()){
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        }
        else{

            List<JoinStorage> optionalJoinStorage = joinStorageRepository.findByProduct(id);
            Long storageId= null;
            if(!optionalJoinStorage.isEmpty()){
            storageId = optionalJoinStorage.get(0).getJoinStorageIdx();}

            List<JoinPeople> optionalJoinPeople = joinPeopleRepository.findByJoinStorage(storageId);
            if(!optionalJoinPeople.isEmpty()){optionalJoinPeople.get(0).getJoinerIdx();}

            joinPeopleRepository.deleteAll(joinPeopleRepository.findByJoinStorage(storageId));
            joinStorageRepository.deleteAll(joinStorageRepository.findByProduct(id));
            productRepository.deleteById(id);


        return null;

        }
    }


    //같이구매 메인조회

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
                List<JoinStorage> optionalJoinStorage = joinStorageRepository.findByProduct(productId);
                Long storageId = null;
                Integer cntOfPeople = null;
                if (!optionalJoinStorage.isEmpty()) {
                    storageId = optionalJoinStorage.get(0).getJoinStorageIdx();
                }

                List<JoinPeople> optionalJoinPeople = joinPeopleRepository.findByJoinStorage(storageId);
                if (!optionalJoinPeople.isEmpty()) {
                    cntOfPeople = optionalJoinPeople.size();
                }

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
                                    .joinPeopleCount(cntOfPeople)
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
                                    .joinPeopleCount(cntOfPeople)
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
                                    .joinPeopleCount(cntOfPeople)
                                    .dealingMethod(product.getDealingMethod())
                                    .build()
                    );
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

    //내가 등록한 같이구매, 내가 참여한 같이구매
    public List<ProductInfo> myGroupBuying(String loginId, String type) {
        Optional<User> user = userRepository.findByLoginId(loginId);
        List<ProductInfo> newList = new ArrayList<>();
        List<Product> loginUserProductList = null;
        List<JoinPeople> loginUserJoinPeopleList = null;
        List<JoinStorage> loginUserJoinStorageList = null;

        if(type.equals("register")) {
            loginUserProductList = productRepository.findByWriter(loginId);
            if (!user.isPresent()) {
                throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
            }
            loginUserProductList.forEach(product -> {
                Long productId = product.getProductIdx();
                List<JoinStorage> optionalJoinStorage = joinStorageRepository.findByProduct(productId);
                Long storageId = null;
                Integer cntOfPeople = null;
                if (!optionalJoinStorage.isEmpty()) {
                    storageId = optionalJoinStorage.get(0).getJoinStorageIdx();
                }

                List<JoinPeople> optionalJoinPeople = joinPeopleRepository.findByJoinStorage(storageId);
                if (!optionalJoinPeople.isEmpty()) {
                    cntOfPeople = optionalJoinPeople.size();
                }

                newList.add(ProductInfo.builder()
                        .productId(product.getProductIdx())
                        .productImageUrl(product.getProductImageUrl())
                        .purchaseStatus(product.getStatus())
                        .productName(product.getProductName())
                        .productPrice(product.getProductPrice())
                        .dividedPrice(product.getProductPrice() / product.getGoalPeopleCount())
                        .goalPeopleCount(product.getGoalPeopleCount())
                        .joinPeopleCount(cntOfPeople)
                        .dealingMethod(product.getDealingMethod())
                        .build());

            });


        }
        else if(type.equals("join")){
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
                if(!people.getJoinStorage().getProduct().getWriter().getLoginId().equals(loginId)){
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

            }}
        );
        }

    return newList;

    }}
