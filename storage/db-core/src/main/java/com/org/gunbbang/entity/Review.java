package com.org.gunbbang.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bakery_id")
    private Bakery bakery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    private Boolean isLike;

    private String reviewText;

    @Builder
    public Review(Bakery bakeryId, Member memberId, Boolean isLike, String reviewText) {
        this.bakery = bakeryId;
        this.member = memberId;
        this.isLike = isLike;
        this.reviewText = reviewText;
    }
}
