package com.org.gunbbang.entity;

import com.org.gunbbang.ReportCategory;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewReport extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewReportId;

  @NotNull private String content;

  @Enumerated(EnumType.STRING)
  @NotNull
  private ReportCategory reportCategory;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  @NotNull
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "review_id")
  @NotNull
  private Review review;

  @Override
  public String toString() {
    return "ReviewReport{"
        + "reviewReportId="
        + reviewReportId
        + ", content='"
        + content
        + '\''
        + ", reportCategory="
        + reportCategory
        + ", member="
        + member
        + ", review="
        + review
        + '}';
  }
}
