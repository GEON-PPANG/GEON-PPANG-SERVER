package com.org.gunbbang.entity;

import javax.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class BakeryNutrientType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bakeryNutrientTypeId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bakery_id")
  private Bakery bakery;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "nutrient_type_id")
  private NutrientType nutrientType;
}
