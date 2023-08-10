package com.org.gunbbang.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
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
}
