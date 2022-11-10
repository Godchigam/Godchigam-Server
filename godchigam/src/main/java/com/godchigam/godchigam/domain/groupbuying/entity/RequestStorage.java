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
@Table(name = "RequestStorage")
public class RequestStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requestStorageIdx_PK")
    private Long requestStorageIdx;

    @OneToOne
    @JoinColumn(name = "userIdx_FK")
    private User user;
}
