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
  @NotNull private List<BreadType> breadType;
  @NotNull private List<NutrientType> nutrientType;
  @NotNull private final AuthType type;

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
        // TODO: breadType, nutrientType 리스트로 넘겨야할 것 같은디.. 양방향으로 해야하나
        //        .breadType(member.getBreadType())
        //        .nutrientType(member.getNutrientType())
        .breadType(null)
        .nutrientType(null)
        .type(authType)
        .build();
  }
}
