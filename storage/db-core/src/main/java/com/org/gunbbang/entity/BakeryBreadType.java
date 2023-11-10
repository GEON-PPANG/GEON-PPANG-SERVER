package com.org.gunbbang.entity;

import javax.persistence.*;

@Entity
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
