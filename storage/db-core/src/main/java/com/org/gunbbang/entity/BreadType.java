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

  @NotNull private boolean isGlutenFree; // 글루텐프리

  @NotNull private boolean isVegan; // 비건빵

  @NotNull private boolean isNutFree; // 넛프리

  @NotNull private boolean isSugarFree; // 저당 및 무설탕

  public boolean getIsGlutenFree() {
    return this.isGlutenFree;
  }

  public boolean getIsVegan() {
    return this.isVegan;
  }

  public boolean getIsNutFree() {
    return this.isNutFree;
  }

  public boolean getIsSugarFree() {
    return this.isSugarFree;
  }
}
