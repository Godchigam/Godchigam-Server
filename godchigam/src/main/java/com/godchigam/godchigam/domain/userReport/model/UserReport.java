package com.godchigam.godchigam.domain.userReport.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import com.godchigam.godchigam.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "UserReport")
public class UserReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reportIdx")
    private Long reportIdx;

    @Column
    private Long reportId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private User user;


}