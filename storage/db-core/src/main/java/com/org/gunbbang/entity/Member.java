package com.org.gunbbang.entity;

import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.PlatformType;
import com.org.gunbbang.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long memberId;

  @NotNull private String email;

  private String password;

  @Enumerated(EnumType.STRING)
  @NotNull
  private PlatformType platformType;

  private String nickname;

  @Enumerated(EnumType.STRING)
  @NotNull
  private Role role;

  @Enumerated(EnumType.STRING)
  private MainPurpose mainPurpose = MainPurpose.NONE;

  private String refreshToken;

  public void updateRefreshToken(String updateRefreshToken) {
    this.refreshToken = updateRefreshToken;
  }

//  public void passwordEncode(PasswordEncoder passwordEncoder) {
//    this.password = passwordEncoder.encode(this.password);
//  }

  //  public void updateBreadType(BreadType breadType) {
  //    this.breadType = breadType;
  //  }
  //
  //  public void updateNutrientType(NutrientType nutrientType) {
  //    this.nutrientType = nutrientType;
  //  }

  public void updateMainPurpose(MainPurpose mainPurpose) {
    this.mainPurpose = mainPurpose;
  }

  public void updateNickname(String nickname) {
    this.nickname = nickname;
  }

  // 유저 권한 설정 메소드
  public void authorizeUser() {
    this.role = Role.ROLE_MEMBER;
  }

  @Builder
  public Member(
      Long memberId,
      String email,
      String password,
      PlatformType platformType,
      String nickname,
      Role role,
      String refreshToken) {
    this.memberId = memberId;
    this.email = email;
    this.password = password;
    this.platformType = platformType;
    this.nickname = nickname;
    this.role = role;
    this.refreshToken = refreshToken;
  }
}
