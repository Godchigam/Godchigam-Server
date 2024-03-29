package com.godchigam.godchigam.domain.groupbuying.entity;

import com.godchigam.godchigam.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "JoinPeople")
public class JoinPeople {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "joinerIdx_PK")
    private Long joinerIdx;

    @Column(nullable = false)
    private String joinUserLoginId;

    @Column(nullable = false)
    private String joinStatus;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "joinStorageIdx_FK")
    private JoinStorage joinStorage;



}
