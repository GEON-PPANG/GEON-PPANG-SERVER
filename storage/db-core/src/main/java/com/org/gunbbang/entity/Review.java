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
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bakery_id")
    private Bakery bakeryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member memberId;

    @NotNull
    private Boolean isLike;

    private String reviewText;

    @Builder
    public Review(Bakery bakeryId, Member memberId, Boolean isLike, String reviewText) {
        this.bakeryId = bakeryId;
        this.memberId = memberId;
        this.isLike = isLike;
        this.reviewText = reviewText;
    }
}
