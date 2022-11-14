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
@Table(name = "JoinStorage")
public class JoinStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "joinStorageIdx_PK")
    private Long joinStorageIdx;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "productIdx_FK")
    private Product product;
}
