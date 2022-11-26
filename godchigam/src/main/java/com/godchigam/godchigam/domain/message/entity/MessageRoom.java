package com.godchigam.godchigam.domain.message.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MessageRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomIdx;

    @Column(nullable = false)
    private Long roomNum;

    @Column(nullable = false)
    private String note;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dateInfo;

    @Column(nullable = false)
    private Long senderIdx;

    @Column(nullable = false)
    private Long receiverIdx;

    @Column(nullable = false)
    private Long owner;



}
