package com.godchigam.godchigam.domain.message.repository;

import com.godchigam.godchigam.domain.groupbuying.entity.JoinStorage;
import com.godchigam.godchigam.domain.groupbuying.entity.RequestStorage;
import com.godchigam.godchigam.domain.message.entity.Message;
import com.godchigam.godchigam.domain.message.entity.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MessageRepository extends JpaRepository<Message,Long> {
    //내가 받은 메세지들
    @Query("select distinct b from Message b where b.receiver= :userIdx")
    List<Message> findByReceiver(@Param("userIdx") Long userIdx);


    //내가 보낸 메세지들
    @Query("select distinct b from Message b where b.sender.userIdx= :userIdx ")
    List<Message> findBySender(@Param("userIdx") Long userIdx);

    //내가 받은 메세지들 중 특정 유저와 한 메세지들
    @Query("select b from Message b where b.receiver= :userId and b.sender.userIdx=:otherId order by b.noteId desc")
    List<Message> findByReceiverAndSender(@Param("userId")Long userId, @Param("otherId")Long otherId);

    //내가 보낸 메세지들 중 특정 유저와 한 메세지들
    @Query("select b from Message b where b.sender.userIdx= :userId and b.receiver=:otherId order by b.noteId desc" )
    List<Message> findByReceiverAndSender1(@Param("userId")Long userId, @Param("otherId")Long otherId);

    @Query("select distinct b from Message b group by b.room")
    List<Message> findByRoom();

    List<Message> findByRoom(@Param("friendId") Long friendId);

    //특정 친구와 나눈 쪽지 중 제일 최근 쪽지
    @Query("select b from Message b where b.room= :friendId order by b.noteId desc")
    List<Message> findByRoom1(@Param("friendId") Long friendId);

}
