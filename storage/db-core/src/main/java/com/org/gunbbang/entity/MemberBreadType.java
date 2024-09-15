package com.org.gunbbang.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

  @Builder
  public MemberBreadType(Member member, BreadType breadType) {
    this.member = member;
    this.breadType = breadType;
  }
}
