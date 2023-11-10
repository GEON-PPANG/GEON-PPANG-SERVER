package com.org.gunbbang.entity;

import javax.persistence.*;

@Entity
public class MemberBreadType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberBreadTypeId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bread_type_id")
  private BreadType breadType;
}
