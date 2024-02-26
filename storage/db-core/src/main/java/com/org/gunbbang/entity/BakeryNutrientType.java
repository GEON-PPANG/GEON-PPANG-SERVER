package com.org.gunbbang.entity;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
    name = "bakeryNutrientType",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "BAKERY_ID_NUTRIENT_TYPE_ID_UNIQUE",
          columnNames = {"bakery_id", "nutrient_type_id"})
    })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
