package com.org.gunbbang.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(
    name = "book_mark",
    uniqueConstraints = {
      @UniqueConstraint(
          columnNames = {"member_id", "bakery_id"},
          name = "uk_member_bakery")
    })
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
}
