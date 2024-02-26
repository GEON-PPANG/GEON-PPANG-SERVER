package com.org.gunbbang.entity;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
    name = "bakeryBreadType",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "BAKERY_ID_BREAD_TYPE_ID_UNIQUE",
          columnNames = {"bakery_id", "bread_type_id"})
    })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BakeryBreadType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bakeryBreadTypeId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bakery_id")
  private Bakery bakery;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bread_type_id")
  private BreadType breadType;
}
