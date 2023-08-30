package com.org.gunbbang.login.service;

import com.org.gunbbang.PlatformType;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.login.CustomOAuth2User;
import com.org.gunbbang.login.OAuthAttributes;
import com.org.gunbbang.repository.MemberRepository;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
  private final MemberRepository memberRepository;

  private static final String KAKAO = "kakao";

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");
    /*
    DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
    DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환
    결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저이다
    * */
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    /*
    userRequest에서 registrationId 추출 후 registrationId에서 platformType 저장
    http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
    userNameAttributeName은 이후에 nameAttributeKey로 설정된다
     */
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    PlatformType platformType = getPlatformType(registrationId);
    String userNameAttributeName =
        userRequest
            .getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName(); // OAuth2 로그인 시 키(PK)가 되는 값
    Map<String, Object> attributes =
        oAuth2User.getAttributes(); // 소셜 로그인 API가 제공하는 userInfo의 Json 값(유저 정보들)

    // platformType에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
    OAuthAttributes extractAttributes =
        OAuthAttributes.of(platformType, userNameAttributeName, attributes);

    Member createdUser =
        getUser(extractAttributes, platformType); // getUser() 메소드로 Member 객체 생성 후 반환

    // DefaultOAuth2User를 구현한 CustomOAuth2User 객체를 생성해서 반환
    return new CustomOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getDesc())),
        attributes,
        extractAttributes.getNameAttributeKey(),
        createdUser.getMemberId(),
        createdUser.getEmail(),
        createdUser.getRole());
  }

  private PlatformType getPlatformType(String registrationId) {
    if (KAKAO.equals(registrationId)) {
      return PlatformType.KAKAO;
    }
    return PlatformType.APPLE;
  }

  // SocialType과 Email을 통해 회원가입한 회원을 찾고 없으면 가입시킨다
  // TODO: Email은 같으나 SocialType이 다르게 들어온 요청의 경우 어떻게 처리해야할 지 고민해야한다
  private Member getUser(OAuthAttributes attributes, PlatformType platformType) {
    return memberRepository
        .findByPlatformTypeAndEmail(platformType, attributes.getOauth2UserInfo().getEmail())
        .orElse(saveUser(attributes, platformType));
  }

  // OAuthAttributes의 toEntity() 메서드를 통해 빌터로 Member 객체 생성 후 반환한다
  // 생성된 Member 객체를 DB에 저장한다: socialType, email, role 값만 있는 상태이다
  private Member saveUser(OAuthAttributes attributes, PlatformType platformType) {
    Member createdUser = attributes.toEntity(platformType, attributes.getOauth2UserInfo());
    return memberRepository.save(createdUser);
  }
}
