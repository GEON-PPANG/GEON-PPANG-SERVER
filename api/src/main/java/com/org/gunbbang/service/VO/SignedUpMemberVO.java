package com.org.gunbbang.service.VO;

import com.org.gunbbang.*;
import com.org.gunbbang.common.AuthType;
import com.org.gunbbang.entity.Member;
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
  //  private final String password;
  //  private final String nickname;
  @NotNull private final PlatformType platformType;
  @NotNull private final Role role;
  @NotNull private final String appleRefreshToken;
  //  private final MainPurpose mainPurpose;
  //  @NotNull private final List<BreadTypeTag> breadTypeTags; // amplitude에 전송할 목적이므로 tag만 보내줌
  //  @NotNull private final List<NutrientTypeTag> nutrientTypeTags;
  @NotNull private final AuthType type;

  public static SignedUpMemberVO of(Member member, String appleRefreshToken, AuthType authType) {
    return SignedUpMemberVO.builder()
        .memberId(member.getMemberId())
        .email(member.getEmail())
        //        .password(member.getPassword()) // 소셜 로그인일 경우 null
        //        .nickname(member.getNickname())
        .platformType(member.getPlatformType())
        .role(member.getRole())
        .appleRefreshToken(appleRefreshToken)
        //        .mainPurpose(member.getMainPurpose())
        //        .breadTypeTags(breadTypeTags)
        //        .nutrientTypeTags(nutrientTypeTags)
        .type(authType)
        .build();
  }
}
