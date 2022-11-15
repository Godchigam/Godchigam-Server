package com.godchigam.godchigam.domain.user.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.godchigam.godchigam.domain.userReport.model.UserReport;
import com.godchigam.godchigam.global.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "USER")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userIdx_PK")
    private Long userIdx;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false) //기본프사 강제 입력 가능하게
    private String profileImageUrl;

    @Column(columnDefinition = "VARCHAR(20) default 'ACTIVE'")
    private String status;

    @Column(nullable = true)
    private String accessToken;

    @Column(nullable = true)
    private String address;

    @JsonBackReference
    @OneToMany(mappedBy = "user",
            fetch = FetchType.LAZY, cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<UserReport> userReports = new ArrayList<>();

    public int getCountOfReports(){return this.userReports.size();}

    public String changeStatus(){return this.status="ACTIVE";}

    public String reChangeStatus(){return this.status="INACTIVE";}
}
