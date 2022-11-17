package com.godchigam.godchigam.domain.message.repository;
import com.godchigam.godchigam.domain.message.entity.Message;
import com.godchigam.godchigam.domain.message.entity.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
public interface MessageRoomRepository extends JpaRepository<MessageRoom,Long>{
    @Query("select distinct b from MessageRoom b group by b.roomNum")
    List<MessageRoom> findByRoomNum();

    //특정 친구와 나눈 쪽지 중 제일 최근 쪽지
    @Query("select b from MessageRoom b where b.roomNum= :friendId order by b.roomIdx desc")
    List<MessageRoom> findByRoomNum1(@Param("friendId") Long friendId);

    List<MessageRoom> findByRoomNum(@Param("friendId") Long friendId);
}
