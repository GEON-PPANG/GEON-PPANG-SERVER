package com.org.gunbbang.login;

import com.org.gunbbang.PlatformType;
import com.org.gunbbang.Role;
import com.org.gunbbang.entity.Member;
import java.util.Map;

import com.org.gunbbang.login.userinfo.KakaoOAuth2UserInfo;
import com.org.gunbbang.login.userinfo.OAuth2UserInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// 소셜별로 받아오는 데이터가 다르므로 데이터 분기 처리하는 DTO 클래스
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class OAuthAttributes {
  private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값
  private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보

  /**
   * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 변환 파라미터: userNameAttributeName -> OAuth2 로그인 시
   * 키가(pk) 되는 값 / attribute OAuth 서비스의 유저 정보들 소셜 별 of 메서드 -> 각각 소셜 로그인 API에서 제공하는 회원의 식별값,
   * attribute, nameAttribute를 저장 후 build
   */
  public static OAuthAttributes of(
      PlatformType platformType, String userNameAttribute, Map<String, Object> attributes) {
    if (platformType == PlatformType.APPLE) {
      //return ofAppple(userNameAttribute, attributes);
    }
    return ofKakao(userNameAttribute, attributes);
  }

  private static OAuthAttributes ofKakao(
      String userNameAttributeName, Map<String, Object> attribute) {
    return OAuthAttributes.builder()
        .nameAttributeKey(userNameAttributeName)
        .oauth2UserInfo(new KakaoOAuth2UserInfo(attribute))
        .build();
  }

  // apple 로그인 필요한 정보 어떤식으로 넘기는지 찾아봐야 함
  //    private static OAuthAttribute ofApple(){
  //
  //    }

  public Member toEntity(PlatformType platformType, OAuth2UserInfo oauth2UserInfo) {
    return
            Member.builder()
                    .platformType(platformType)
            .email(oauth2UserInfo.getEmail())
            .nickname("GUEST")
                    .role(Role.GUEST)
    .build();
  }
}
