package com.org.gunbbang.entity;

import lombok.AccessLevel;
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
    private Review reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommend_keyword_id")
    @NotNull
    private RecommendKeyword recommendKeywordId;
}
