package com.org.gunbbang.login.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.PlatformType;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.login.KakaoUserResponse;
import com.org.gunbbang.repository.BreadTypeRepository;
import com.org.gunbbang.repository.MemberRepository;
import com.org.gunbbang.repository.NutrientTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class KakaoAuthService extends AuthService {

  @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
  private String kakaoUserInfoUri;

  public KakaoAuthService(
      MemberRepository memberRepository,
      BreadTypeRepository breadTypeRepository,
      NutrientTypeRepository nutrientTypeRepository) {
    super(memberRepository, breadTypeRepository, nutrientTypeRepository);
  }

  @Override
  public Member loadMemberByToken(String accessToken, PlatformType platformType)
      throws JsonProcessingException {
    RestTemplate restTemplate = new RestTemplate();

    // 카카오 API로 사용자 정보 요청을 보낼 때 액세스 토큰을 Bearer 토큰 스키마로 전달하기 위한 헤더 설정
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);
    // 헤더를 포함한 HttpEntity 생성
    HttpEntity<String> entity = new HttpEntity<>(headers);

    // HttpEntity를 사용하여 API 요청을 보내고 응답을 받음
    // ResponseEntity<String> 타입으로 response를 받음
    String response =
        restTemplate.exchange(kakaoUserInfoUri, HttpMethod.GET, entity, String.class).getBody();

    ObjectMapper objectMapper = new ObjectMapper();

    // KakaoOAuthUserInfo 로 반환 받은 값으로 바로 회원가입되게 변경
    KakaoUserResponse userInfoResponse = objectMapper.readValue(response, KakaoUserResponse.class);

    // 기타 사용자 정보를 attributes에 추가
    return getUser(
        platformType,
        userInfoResponse.getKakaoAccount().getEmail()); // getUser() 메소드로 Member 객체 생성 후 반환
  }
}
