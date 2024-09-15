package com.org.gunbbang.entity;

import com.org.gunbbang.NutrientTypeTag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

  @NotNull
  @Enumerated(EnumType.STRING)
  private NutrientTypeTag nutrientTypeTag;
}
