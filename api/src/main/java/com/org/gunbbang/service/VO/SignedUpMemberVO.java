package com.org.gunbbang.service.VO;

import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.PlatformType;
import com.org.gunbbang.Role;
import com.org.gunbbang.common.AuthType;
import com.org.gunbbang.entity.BreadType;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.NutrientType;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Builder
@Getter
@ToString
public class SignedUpMemberVO {
  private final Long memberId;
  private final String email;
  private final String password;
  private final String nickname;
  @NotNull private final PlatformType platformType;
  @NotNull private final Role role;
  @NotNull private final String appleRefreshToken;
  private final MainPurpose mainPurpose;
  @NotNull private final List<BreadType> breadType;
  @NotNull private final List<NutrientType> nutrientType;
  @NotNull private final AuthType type;

  // TODO: breadType, nutrientType 리스트로 넘겨야할 것 같은디 양방향으로 할지 아니면 그냥 단방향으로 할지 고민
  public static SignedUpMemberVO of(Member member, String appleRefreshToken, AuthType authType) {
    return SignedUpMemberVO.builder()
        .memberId(member.getMemberId())
        .email(member.getEmail())
        .password(member.getPassword()) // 소셜 로그인일 경우 null
        .nickname(member.getNickname())
        .platformType(member.getPlatformType())
        .role(member.getRole())
        .appleRefreshToken(appleRefreshToken)
        .mainPurpose(member.getMainPurpose())
        .breadType(null)
        .nutrientType(null)
        .type(authType)
        .build();
  }
}
