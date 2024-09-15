package com.org.gunbbang.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberNutrientType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberNutrientTypeId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "nutrient_type_id")
  private NutrientType nutrientType;

  @Builder
  public MemberNutrientType(Member member, NutrientType nutrientType) {
    this.member = member;
    this.nutrientType = nutrientType;
  }
}
