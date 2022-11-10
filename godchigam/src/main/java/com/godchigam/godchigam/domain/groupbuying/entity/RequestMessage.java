package com.godchigam.godchigam.domain.groupbuying.entity;

import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.global.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "RequestMessage")
public class RequestMessage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requestIdx_PK")
    private Long requestIdx;

    @ManyToOne
    @JoinColumn(name = "requestSendUserIdx_FK")
    private User requestSendUser;

    @Column(nullable = false)
    private String requestType;

    @ManyToOne
    @JoinColumn(name = "productIdx_FK")
    private Product requestProduct;

    @ManyToOne
    @JoinColumn(name = "requestStorageIdx_FK")
    private RequestStorage requestStorage;
}
