package com.org.gunbbang.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendKeyword {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long recommendKeywordId;

  @NotNull private String keywordName;
}
