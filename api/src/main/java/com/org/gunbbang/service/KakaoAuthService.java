package com.org.gunbbang.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.support.exception.InvalidKakaoTokenException;
import com.org.gunbbang.common.AuthType;
import com.org.gunbbang.controller.DTO.request.MemberSignUpRequestDTO;
import com.org.gunbbang.entity.KakaoMember;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.support.errorType.ErrorType;
import com.org.gunbbang.repository.KakaoMemberRepository;
import com.org.gunbbang.repository.MemberRepository;
import com.org.gunbbang.service.VO.KakaoUserVO;
import com.org.gunbbang.service.VO.SignedUpMemberVO;
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

  private final KakaoMemberRepository kakaoMemberRepository;

  public KakaoAuthService(
      MemberRepository memberRepository, KakaoMemberRepository kakaoMemberRepository) {
    super(memberRepository);
    this.kakaoMemberRepository = kakaoMemberRepository;
  }

  @Override
  public SignedUpMemberVO saveMemberOrLogin(String platformToken, MemberSignUpRequestDTO request)
      throws JsonProcessingException {
    RestTemplate restTemplate = new RestTemplate();

    // 카카오 API로 사용자 정보 요청을 보낼 때 액세스 토큰을 Bearer 토큰 스키마로 전달하기 위한 헤더 설정
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + platformToken);
    // 헤더를 포함한 HttpEntity 생성
    HttpEntity<String> entity = new HttpEntity<>(headers);

    // HttpEntity를 사용하여 API 요청을 보내고 응답을 받음
    // ResponseEntity<String> 타입으로 response를 받음
    try {
      String response =
          restTemplate.exchange(kakaoUserInfoUri, HttpMethod.GET, entity, String.class).getBody();

      ObjectMapper objectMapper = new ObjectMapper();

      // KakaoOAuthUserInfo 로 반환 받은 값으로 바로 회원가입되게 변경
      KakaoUserVO userInfoVO = objectMapper.readValue(response, KakaoUserVO.class);
      System.out.println("userInfoVO: " + userInfoVO.toString());

      Member foundMember =
          getUser(request.getPlatformType(), userInfoVO.getKakaoAccount().getEmail());

      if (foundMember != null) {
        return SignedUpMemberVO.of(foundMember, null, AuthType.LOGIN);
      }

      Member savedMember = saveUser(request, userInfoVO.getKakaoAccount().getEmail());

      if (!userInfoVO.getKakaoAccount().getAge_range_needs_agreement()
          || !userInfoVO.getKakaoAccount().getGender_needs_agreement()) {
        saveKakaoMemberInfo(userInfoVO, savedMember);
      }
      return SignedUpMemberVO.of(savedMember, null, AuthType.SIGN_UP);

    } catch (Exception e) {
      throw new InvalidKakaoTokenException(
          ErrorType.INVALID_KAKAO_TOKEN_EXCEPTION,
          ErrorType.INVALID_KAKAO_TOKEN_EXCEPTION.getMessage());
    }
  }

  private void saveKakaoMemberInfo(KakaoUserVO kakaoUserVo, Member member) {
    KakaoMember newKakaoMember =
        KakaoMember.builder()
            .member(member)
            .gender(kakaoUserVo.getKakaoAccount().getGender())
            .age_range(kakaoUserVo.getKakaoAccount().getAge_range())
            .build();
    kakaoMemberRepository.save(newKakaoMember);
  }
}
