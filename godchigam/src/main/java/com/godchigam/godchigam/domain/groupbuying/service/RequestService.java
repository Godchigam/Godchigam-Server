package com.godchigam.godchigam.domain.groupbuying.service;

import com.godchigam.godchigam.domain.groupbuying.dto.RequestMessageResponse;
import com.godchigam.godchigam.domain.groupbuying.dto.UserInfoResponse;
import com.godchigam.godchigam.domain.groupbuying.dto.UserJoinStatusResponse;
import com.godchigam.godchigam.domain.groupbuying.entity.JoinPeople;
import com.godchigam.godchigam.domain.groupbuying.entity.JoinStorage;
import com.godchigam.godchigam.domain.groupbuying.entity.RequestMessage;
import com.godchigam.godchigam.domain.groupbuying.entity.RequestStorage;
import com.godchigam.godchigam.domain.groupbuying.repository.JoinStorageRepository;
import com.godchigam.godchigam.domain.groupbuying.repository.RequestRepository;
import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.domain.user.repository.UserRepository;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
}
