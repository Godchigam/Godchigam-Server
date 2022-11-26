package com.godchigam.godchigam.domain.message.service;
import com.godchigam.godchigam.domain.groupbuying.dto.ProductInfo;
import com.godchigam.godchigam.domain.groupbuying.entity.Product;
import com.godchigam.godchigam.domain.groupbuying.repository.ProductRepository;
import com.godchigam.godchigam.domain.message.dto.FriendInfo;
import com.godchigam.godchigam.domain.message.dto.MessageRequest;
import com.godchigam.godchigam.domain.message.dto.MessageResponse;
import com.godchigam.godchigam.domain.message.dto.SpecificMessageReponse;
import com.godchigam.godchigam.domain.message.entity.Message;
import com.godchigam.godchigam.domain.message.entity.MessageRoom;
import com.godchigam.godchigam.domain.message.repository.MessageRepository;
import com.godchigam.godchigam.domain.message.repository.MessageRoomRepository;
import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.domain.user.repository.UserRepository;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import com.godchigam.godchigam.global.dto.DateInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MessageService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageRoomRepository messageRoomRepository;


    //쪽지 보내기
    public Message createMessage(MessageRequest messageRequest, String userId) {
        Optional<User> user = userRepository.findByLoginId(userId);
        Long userIdx = user.get().getUserIdx();//현재 로그인한 값
        LocalDate currentTime = LocalDate.now();
        List<User> receiverList = new ArrayList<>();
        if (!user.isPresent()) {
            throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
        }

        Long[] arr = messageRequest.getUsers();
        List<Long> list = Arrays.asList(arr);
        list.forEach(requestId -> {
            if(userRepository.findByUserIdx(requestId).isEmpty()){
                throw new BaseException(ErrorCode.USERS_EMPTY_USER_ID);
            }
            Boolean isReceived = Boolean.TRUE;

            Message messageToCreate = new Message();
            BeanUtils.copyProperties(messageRequest, messageToCreate);
            MessageRoom roomToCreate = new MessageRoom();
            BeanUtils.copyProperties(messageRequest, roomToCreate);


            messageToCreate.setSender(user.get());
            messageToCreate.setReceiver(requestId);
            messageToCreate.setNote(messageRequest.getNote());


            if(messageToCreate.getSender().equals(userIdx)){
                isReceived=Boolean.FALSE; }


            messageToCreate.setDateInfo(currentTime);

            roomToCreate.setNote(messageRequest.getNote());
            roomToCreate.setDateInfo(currentTime);
            roomToCreate.setSenderIdx(userIdx);
            roomToCreate.setReceiverIdx(requestId);
          //  roomToCreate.setIsReceived(Boolean.FALSE);
            //메세지 시작, 룸 최초로 만들어짐
            if(messageRepository.findByReceiverAndSender(userIdx,requestId).isEmpty()&&messageRepository.findByReceiverAndSender1(userIdx,requestId).isEmpty()) {
                log.info("내가 특정 상대에게 맨 처음 보내는 메세지");
                messageToCreate.setRoom(requestId);
                messageToCreate.setOwner(userIdx);
                roomToCreate.setRoomNum(requestId);
                roomToCreate.setOwner(userIdx);
                messageRepository.save(messageToCreate);
                messageRoomRepository.save(roomToCreate);

            }
            //보냈는데 답장이 없고 계속 보내는 경우
            else if(messageRepository.findByReceiverAndSender(userIdx,requestId).isEmpty()&&!messageRepository.findByReceiverAndSender1(userIdx,requestId).isEmpty()){
                log.info("상대 답장 없을 때 내가 특정 상대에게 보내는 메세지");
                messageToCreate.setRoom(requestId);
                messageToCreate.setOwner(userIdx);
                roomToCreate.setRoomNum(requestId);
                roomToCreate.setOwner(userIdx);
                messageRepository.save(messageToCreate);
                messageRoomRepository.save(roomToCreate);
            }

            else{log.info("메세지 시작");

                if(!messageToCreate.getSender().getUserIdx().equals(messageRepository.findByReceiverAndSender(userIdx, requestId).get(0).getOwner())){

                    messageToCreate.setRoom(userIdx);
                    messageToCreate.setOwner(requestId);
                    roomToCreate.setRoomNum(userIdx);
                    roomToCreate.setOwner(requestId);
                    messageRepository.save(messageToCreate);
                    messageRoomRepository.save(roomToCreate);
                }
                else if(messageToCreate.getSender().getUserIdx().equals(messageRepository.findByReceiver(userIdx).get(0).getRoom())&&!userIdx.equals(messageToCreate.getRoom()))
                {
                    log.info("다른사람이 나에게 답장");

                    messageToCreate.setRoom(userIdx);
                    messageToCreate.setOwner(requestId);
                    roomToCreate.setRoomNum(userIdx);
                    roomToCreate.setOwner(requestId);
                    log.info(String.valueOf(messageToCreate.getRoom()));
                    log.info(String.valueOf(roomToCreate.getRoomNum()));
                    messageRepository.save(messageToCreate);
                    messageRoomRepository.save(roomToCreate);
                }else{
                    log.info("내가 다시 답장");
                    messageToCreate.setRoom(requestId);
                    messageToCreate.setOwner(userIdx);
                    roomToCreate.setRoomNum(requestId);
                    roomToCreate.setOwner(userIdx);
                    messageRepository.save(messageToCreate);
                    messageRoomRepository.save(roomToCreate);
                }

            }

            });
        return null;
    }

    public List<MessageResponse> readMyMessage(String userId){
        Optional<User> user = userRepository.findByLoginId(userId);
        Long userIdx = user.get().getUserIdx();
        List<MessageRoom> messageRooms = messageRoomRepository.findByRoomNum();
        List<Message> messages = messageRepository.findByRoomAndOwner(userIdx);
        List<Message> messages2 = messageRepository.findByRoom(userIdx);

        List<MessageResponse> newList = new ArrayList<>();


        if(!messages.isEmpty()||!messages2.isEmpty()){
      //  if(messageRepository.findByOwner(userIdx).isEmpty()||(!messageRepository.findByOwner(userIdx).isEmpty()&&!messageRepository.findByRoom(userIdx).isEmpty())){

            messages2.forEach(room->{
                Long friendId = room.getOwner();
                String friendImageUrl = userRepository.findByUserIdx(friendId).get().getProfileImageUrl();
                String friendNickName = userRepository.findByUserIdx(friendId).get().getNickname();
                List<Message> messageList = messageRepository.findByRoom2(userIdx, friendId);
                Long noteId = messageList.get(0).getNoteId();
                String recentMessage1 = messageList.get(0).getNote();


                newList.add(
                        MessageResponse.builder()
                                .noteId(noteId)
                                .friendInfo(FriendInfo.builder()
                                        .userId(friendId)
                                        .profileImageUrl(friendImageUrl)
                                        .nickname(friendNickName)
                                        .build())
                                .recentNote(recentMessage1)
                                .build()

                );
            });

    //    }
   //     else {
            messages.forEach(room -> {
                if (room.getSender().getUserIdx().equals(userIdx) || room.getReceiver().equals(userIdx)) {

                    Long friendId = room.getRoom();
                    String friendImageUrl = userRepository.findByUserIdx(friendId).get().getProfileImageUrl();
                    String friendNickName = userRepository.findByUserIdx(friendId).get().getNickname();
                    List<MessageRoom> messageRoomList = messageRoomRepository.findByRoomNum1(friendId);
                    List<Message> messageList = messageRepository.findByRoom1(userIdx, friendId);

                    String recentMessage = messageRoomList.get(0).getNote();
                    String recentMessage1 = messageList.get(0).getNote();
                    Long noteId = messageList.get(0).getNoteId();
                    newList.add(
                            MessageResponse.builder()
                                    .noteId(noteId)
                                    .friendInfo(FriendInfo.builder()
                                            .userId(friendId)
                                            .profileImageUrl(friendImageUrl)
                                            .nickname(friendNickName)
                                            .build())
                                    .recentNote(recentMessage1)
                                    .build()

                    );
                }
            }
            );
     //   }
    }

    return newList;
    }


    public List<SpecificMessageReponse> readMessage(String userId, Long friendId){
        Optional<User> user = userRepository.findByLoginId(userId);
        Long userIdx = user.get().getUserIdx();
        List<Message> messages = messageRepository.findByRoomAndOwner2(userIdx, friendId);
        List<Message> messages2 = messageRepository.findByRoom3(userIdx, friendId);
        log.info(String.valueOf(messages));
        Collections.reverse(messages);
        Collections.reverse(messages2);
        log.info(String.valueOf(messages));
        List<SpecificMessageReponse> newList = new ArrayList<>();

        if(!messages.isEmpty()||!messages2.isEmpty()){
            messages2.forEach(message -> {
                Boolean isReceived = Boolean.FALSE;
                if (!message.getReceiver().equals(message.getOwner())) {
                    isReceived = Boolean.TRUE;
                }
                newList.add(
                        SpecificMessageReponse.builder()
                                .note(message.getNote())
                                .dateInfo(DateInfoResponse.builder()
                                        .year(message.getDateInfo().getYear())
                                        .month(message.getDateInfo().getMonthValue())
                                        .day(message.getDateInfo().getDayOfMonth())
                                        .build())
                                .isReceived(isReceived)
                                .build()
                );
            });
        //}
        //else {
            messages.forEach(message -> {
                Boolean isReceived = Boolean.FALSE;
                if (message.getSender().getUserIdx().equals(friendId)) {
                    isReceived = Boolean.TRUE;
                }
                newList.add(
                        SpecificMessageReponse.builder()
                                .note(message.getNote())
                                .dateInfo(DateInfoResponse.builder()
                                        .year(message.getDateInfo().getYear())
                                        .month(message.getDateInfo().getMonthValue())
                                        .day(message.getDateInfo().getDayOfMonth())
                                        .build())
                                .isReceived(isReceived)
                                .build()
                );

            });

        }
        return newList;
    }



}
