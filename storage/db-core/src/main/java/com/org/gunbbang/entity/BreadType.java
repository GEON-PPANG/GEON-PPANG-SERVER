package com.org.gunbbang.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BreadType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "bread_type_id")
  private Long breadTypeId;

  @NotNull private String breadTypeName;

  @NotNull private Boolean isGlutenFree; // 글루텐프리

  @NotNull private Boolean isVegan; // 비건빵

  @NotNull private Boolean isNutFree; // 넛프리

  @NotNull private Boolean isSugarFree; // 저당 및 무설탕
}
