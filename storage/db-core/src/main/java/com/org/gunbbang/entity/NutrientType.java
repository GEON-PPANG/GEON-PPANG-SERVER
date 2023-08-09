package com.org.gunbbang.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NutrientType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long nutrientTypeId;

  @NotNull private String nutrientTypeName;

  @NotNull private Boolean isNutrientOpen; // 영양성분 공개

  @NotNull private Boolean isIngredientOpen; // 원재료 공개

  @NotNull private Boolean isNotOpen; // 비공개
}
