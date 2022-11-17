package com.godchigam.godchigam.domain.message.entity;
import com.godchigam.godchigam.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;

    @Column(nullable = false)
    private String note;


  //  @Column(nullable = false)
  //  private Boolean isReceived;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dateInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User sender;


    @Column(nullable = false)
    private Long receiver;

    @Column(nullable = false)
    private Long room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_room")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private MessageRoom messageRoom;


}
