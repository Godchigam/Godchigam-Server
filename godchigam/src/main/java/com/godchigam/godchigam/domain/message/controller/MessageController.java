package com.godchigam.godchigam.domain.message.controller;

import com.godchigam.godchigam.domain.groupbuying.entity.Product;
import com.godchigam.godchigam.domain.message.dto.MessageRequest;
import com.godchigam.godchigam.domain.message.dto.MessageResponse;
import com.godchigam.godchigam.domain.message.dto.SpecificMessageReponse;
import com.godchigam.godchigam.domain.message.entity.Message;
import com.godchigam.godchigam.domain.message.service.MessageService;
import com.godchigam.godchigam.domain.user.repository.UserRepository;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import com.godchigam.godchigam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping(value = "/community")
@RequiredArgsConstructor
public class MessageController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MessageService messageService;
    private final UserRepository userRepository;

    @PostMapping("/message")
    public CommonResponse<Message> createMessage(@RequestHeader("token") String accessToken,@RequestBody MessageRequest request){
        String userId = jwtTokenProvider.getUserLoginId(accessToken);
        Long[] arr = request.getUsers();
        List<Long> list = Arrays.asList(arr);
        list.forEach(id->{
            if(userRepository.findByUserIdx(id).isEmpty()){
                throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
                //에러처리
            }
            Long userIdx = userRepository.findByLoginId(userId).get().getUserIdx();
            if(id.equals(userIdx)){
                throw new BaseException(ErrorCode.WRONG_MESSAGE_USER_ID);
            }
        });
        return CommonResponse.success(messageService.createMessage(request,userId),"쪽지 전송 성공");
    }

    @GetMapping("/note")
    public CommonResponse<List<MessageResponse>> readMyMessage(@RequestHeader("token") String accessToken){
        String userId = jwtTokenProvider.getUserLoginId(accessToken);
        return CommonResponse.success(messageService.readMyMessage(userId),"나의 쪽지함 조회 성공");
    }

    @GetMapping("note/{friendId}")
    public CommonResponse<List<SpecificMessageReponse>> readFriendMessage(@RequestHeader("token") String accessToken,@PathVariable Long friendId) {
        String userId = jwtTokenProvider.getUserLoginId(accessToken);
        if(userRepository.findByUserIdx(friendId).isEmpty()){
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        }
        Long userIdx = userRepository.findByLoginId(userId).get().getUserIdx();
        if(friendId.equals(userIdx)){
            throw new BaseException(ErrorCode.WRONG_MESSAGE_USER_ID); 
        }
        return CommonResponse.success(messageService.readMessage(userId, friendId),"타 유저와의 쪽지함 조회 성공");

    }
}
