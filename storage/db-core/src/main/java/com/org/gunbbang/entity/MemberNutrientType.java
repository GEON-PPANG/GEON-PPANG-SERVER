package com.org.gunbbang.entity;

import javax.persistence.*;

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
}
