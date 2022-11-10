package com.godchigam.godchigam.domain.groupbuying.service;

import com.godchigam.godchigam.domain.groupbuying.dto.RequestMessageResponse;
import com.godchigam.godchigam.domain.groupbuying.dto.UserInfoResponse;
import com.godchigam.godchigam.domain.groupbuying.entity.RequestMessage;
import com.godchigam.godchigam.domain.groupbuying.entity.RequestStorage;
import com.godchigam.godchigam.domain.groupbuying.repository.RequestRepository;
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
public class RequestService {

    private final RequestRepository requestRepository;

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
}
