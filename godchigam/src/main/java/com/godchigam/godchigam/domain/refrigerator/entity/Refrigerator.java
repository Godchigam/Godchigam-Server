package com.godchigam.godchigam.domain.refrigerator.entity;


import com.godchigam.godchigam.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "REFRIGERATOR")
public class Refrigerator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "frigeIdx_PK")
    private Long frigeIdx;

    @OneToOne
    @JoinColumn(name = "userIdx_FK")
    private User user;
}
