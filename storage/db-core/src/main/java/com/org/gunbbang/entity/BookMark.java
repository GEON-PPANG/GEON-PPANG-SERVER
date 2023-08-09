package com.org.gunbbang.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMark {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bookMarkId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  @NotNull
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bakery_id")
  @NotNull
  private Bakery bakery;

  @Builder
  public BookMark(Member member, Bakery bakery) {
    this.member = member;
    this.bakery = bakery;
  }
}
