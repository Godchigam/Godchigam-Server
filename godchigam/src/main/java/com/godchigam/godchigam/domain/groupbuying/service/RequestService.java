package com.godchigam.godchigam.domain.groupbuying.service;

import com.godchigam.godchigam.domain.groupbuying.dto.*;
import com.godchigam.godchigam.domain.groupbuying.entity.*;
import com.godchigam.godchigam.domain.groupbuying.repository.JoinPeopleRepository;
import com.godchigam.godchigam.domain.groupbuying.repository.JoinStorageRepository;
import com.godchigam.godchigam.domain.groupbuying.repository.RequestMessageRepository;
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
    private final RequestMessageRepository requestMessageRepository;

    private final ProductRepository productRepository;
    private final UserReportRepository userReportRepository;


    public List<RequestMessageResponse> LookUpRequestStorage(String loginId) {

        Optional<RequestStorage> loginUserStorage = requestRepository.findByUser(loginId);
        Long loginUserStorageIdx = loginUserStorage.get().getRequestStorageIdx();
        log.info(String.valueOf(loginUserStorageIdx));

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

        if(resultMessageList.isEmpty()) return null;
        return resultMessageList;
    }

    public UserJoinStatusResponse checkProductJoinStatus(String loginId, Long productId) {

        Optional<JoinStorage> joinStorage = joinStorageRepository.findByProduct(productId);
        if (joinStorage.isEmpty()) {
            throw new BaseException(ErrorCode.EMPTY_PRODUCT_ID);
        }

        Long joinStorageId = joinStorage.get().getJoinStorageIdx();
        List<JoinPeople> joinPeopleList = joinStorageRepository.findByJoinStorageIdx(joinStorageId);

        UserJoinStatusResponse resultResponse = new UserJoinStatusResponse("참여안함");

        joinPeopleList.forEach(joinPeople -> {
            if (joinPeople.getJoinUserLoginId().equals(loginId)) {
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
                .isOwner(isOwner)
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

            if (product.getGoalPeopleCount().equals(joinnerList.size())) {
                changeProductStatus.setPurchaseStatus("모집완료");
            } else {
                changeProductStatus.setPurchaseStatus("모집중");
            }

        } else {
            changeProductStatus.setPurchaseStatus("종료");
        }
        return changeProductStatus;
    }

    /**
     * RequestMessage 추가 되도록 repo 로직 추가해야함.
     */
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

        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setRequestProduct(joinStorage.get().getProduct());
        requestMessage.setRequestSendUser(writer.get());
        Optional<RequestStorage> requestStorage = requestRepository.findByUser(joinStorage.get().getProduct().getWriter().getLoginId());
        requestMessage.setRequestStorage(requestStorage.get());

        String myJoinType = "";
        if (checkMyStatus.isEmpty()) {
            myJoinType = "참여대기";
            JoinPeople newJoinner = new JoinPeople();
            newJoinner.setJoinUserLoginId(loginId);
            newJoinner.setJoinStatus("참여대기");
            newJoinner.setJoinStorage(joinStorage.get());

            requestMessage.setRequestType("참여대기");
            joinPeopleRepository.save(newJoinner);

        } else {
            JoinPeople currentUser = checkMyStatus.get(0);
            myJoinType = currentUser.getJoinStatus(); //참여중 일수도 있고 참여안함 일수도 있음

            if (myJoinType.equals("참여중")) {
                currentUser.setJoinStatus("탈퇴대기");
                requestMessage.setRequestType("탈퇴대기");
                myJoinType = "탈퇴대기";
            } else if (myJoinType.equals("참여안함")) {
                currentUser.setJoinStatus("참여대기");
                requestMessage.setRequestType("참여대기");
                myJoinType = "참여대기";
            }

            joinPeopleRepository.save(currentUser);
        }

        requestMessageRepository.save(requestMessage);
        JoinStatusResponse joinStatusResponse = new JoinStatusResponse(myJoinType);
        return joinStatusResponse;
    }

    public JoinStatusResponse sendJoinCancel(String loginId, Long productId) {

        Optional<User> requestSender = userRepository.findByLoginId(loginId);
        if (requestSender.isEmpty()) {
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        }

        Optional<JoinStorage> joinStorage = joinStorageRepository.findByProduct(productId);
        if (joinStorage.isEmpty()) {
            throw new BaseException(ErrorCode.EMPTY_PRODUCT_ID);
        }

        User owner = joinStorage.get().getProduct().getWriter();
        Long joinStorageId = joinStorage.get().getJoinStorageIdx();
        List<JoinPeople> joinPeopleList = joinStorageRepository.findByJoinStorageIdx(joinStorageId);

        List<JoinPeople> checkMyStatus = new ArrayList<>();

        joinPeopleList.forEach(joinPeople -> {
            if (joinPeople.getJoinUserLoginId().equals(loginId)) {
                checkMyStatus.add(joinPeople);
            }
        });

        JoinPeople currentUser = checkMyStatus.get(0);
        String myJoinType = currentUser.getJoinStatus();
        String newJoinType = "";
        if (myJoinType.equals("참여대기")) {
            newJoinType = "참여안함";
        } else { //탈퇴 대기
            newJoinType = "참여중";
        }

        currentUser.setJoinStatus(newJoinType);
        joinPeopleRepository.save(currentUser);

        //기존 요청 삭제하기
        Optional<RequestStorage> requestStorage = requestRepository.findByUser(owner.getLoginId());
        Optional<RequestMessage> pastRequest = requestRepository.findByRequestStorageIdxAndSenderLoginId(requestStorage.get().getRequestStorageIdx(), loginId);
        log.info("리퀘스트 존재"+pastRequest.isEmpty());
        requestMessageRepository.delete(pastRequest.get());

        JoinStatusResponse joinStatusResponse = new JoinStatusResponse(newJoinType);
        return joinStatusResponse;
    }

    public void checkRequest(String loginId, CheckRequest checkRequest) {

        Long requestId = checkRequest.getRequestId();

        Optional<RequestStorage> writerStorage = requestRepository.findByUser(loginId);
        Optional<RequestMessage> selectedRequest = requestRepository.findByRequestStorageIdxAndRequestMessageIdx(writerStorage.get().getRequestStorageIdx(), requestId);

        if (selectedRequest.isEmpty()) {
            throw new BaseException(ErrorCode.EMPTY_REQUEST_ID);
        }

        //요청 보낸 사람의 상태값 바뀜.
        Long sendUserIdx = checkRequest.getUserId();
        Optional<User> sendUser = userRepository.findByUserIdx(sendUserIdx);
        Long productId = checkRequest.getProductId();
        Optional<JoinStorage> currentJoinStorage = joinStorageRepository.findByProduct(productId);
        List<JoinPeople> currentJoinPeople = joinStorageRepository.findByJoinStorageIdx(currentJoinStorage.get().getJoinStorageIdx());

        currentJoinPeople.forEach(joinPeople -> {
            if (joinPeople.getJoinUserLoginId().equals(sendUser.get().getLoginId())) {
                if (checkRequest.getRequestType().equals("참여")) {
                    joinPeople.setJoinStatus("참여중");
                } else {
                    joinPeople.setJoinStatus("참여안함");
                }
            }
        });

        //요청함에서 요청 삭제됨
        requestMessageRepository.delete(selectedRequest.get());
    }

}

