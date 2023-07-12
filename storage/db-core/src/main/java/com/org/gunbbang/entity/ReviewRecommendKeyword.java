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
public class ReviewRecommendKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewRecommendKeywordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    @NotNull
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommend_keyword_id")
    @NotNull
    private RecommendKeyword recommendKeyword;

    @Builder
    public ReviewRecommendKeyword(Review review, RecommendKeyword recommendKeyword) {
        this.review = review;
        this.recommendKeyword = recommendKeyword;
    }
}
