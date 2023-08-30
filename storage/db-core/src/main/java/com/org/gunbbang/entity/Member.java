package com.org.gunbbang.entity;

import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.PlatformType;
import com.org.gunbbang.Role;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
// @Builder
public class Member extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long memberId;

  @NotNull private String email;

  // @NotNull
  private String password;

  @Enumerated(EnumType.STRING)
  // @NotNull
  private PlatformType platformType;

  @NotNull private String nickname;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Enumerated(EnumType.STRING)
  private MainPurpose mainPurpose = MainPurpose.NONE;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bread_type_id")
  // @NotNull
  private BreadType breadType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "nutrient_type_id")
  // @NotNull
  private NutrientType nutrientType;

  private String refreshToken;

  public void updateRefreshToken(String updateRefreshToken) {
    this.refreshToken = updateRefreshToken;
  }

  // TODO: 이거 모듈 관련해서 의존성 추가하는 문제 생각해봐야함
  public void passwordEncode(PasswordEncoder passwordEncoder) {
    this.password = passwordEncoder.encode(this.password);
  }

  public void updateBreadType(BreadType breadType) {
    this.breadType = breadType;
  }

  public void updateNutrientType(NutrientType nutrientType) {
    this.nutrientType = nutrientType;
  }

  public void updateMainPurpose(MainPurpose mainPurpose) {
    this.mainPurpose = mainPurpose;
  }

  // 유저 권한 설정 메소드
  public void authorizeUser() {
    this.role = Role.USER;
  }

  @Builder
  public Member(
      Long memberId,
      String email,
      String password,
      PlatformType platformType,
      String nickname,
      Role role,
      BreadType breadType,
      NutrientType nutrientType,
      String refreshToken) {
    this.memberId = memberId;
    this.email = email;
    this.password = password;
    this.platformType = platformType;
    this.nickname = nickname;
    this.role = role;
    this.breadType = breadType;
    this.nutrientType = nutrientType;
    this.refreshToken = refreshToken;
  }
}
