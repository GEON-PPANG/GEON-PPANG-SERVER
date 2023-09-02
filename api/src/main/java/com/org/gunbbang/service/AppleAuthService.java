package com.org.gunbbang.service;

import com.org.gunbbang.DTO.AppleAuthResponseDTO;
import com.org.gunbbang.common.AuthType;
import com.org.gunbbang.controller.DTO.request.MemberSignUpRequestDTO;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.jwt.util.AppleJwtService;
import com.org.gunbbang.repository.BreadTypeRepository;
import com.org.gunbbang.repository.MemberRepository;
import com.org.gunbbang.repository.NutrientTypeRepository;
import com.org.gunbbang.service.VO.SignedUpMemberVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AppleAuthService extends AuthService {
  private final AppleJwtService appleJwtService;

  public AppleAuthService(
      MemberRepository memberRepository,
      BreadTypeRepository breadTypeRepository,
      NutrientTypeRepository nutrientTypeRepository,
      AppleJwtService appleJwtService) {
    super(memberRepository, breadTypeRepository, nutrientTypeRepository);
    this.appleJwtService = appleJwtService;
  }

  @Override
  public SignedUpMemberVO saveMemberOrLogin(String platformToken, MemberSignUpRequestDTO request)
      throws Exception {

    log.info("#######  validateAuthorizationCode 시작  #######");
    AppleAuthResponseDTO response = appleJwtService.validateAuthorizationCode(platformToken);
    log.info("#######  validateAuthorizationCode 끝  #######");

    log.info("#######  getEmailFromIdentityToken 시작  #######");
    String email = appleJwtService.getEmailFromIdentityToken(response.getId_token());
    log.info("#######  getEmailFromIdentityToken 끝  #######");

    Member foundMember = getUser(request.getPlatformType(), email);
    if (foundMember != null) {
      return SignedUpMemberVO.of(foundMember, response.getRefresh_token(), AuthType.LOGIN);
    }

    Member savedMember = saveUser(request, email);
    return SignedUpMemberVO.of(savedMember, response.getRefresh_token(), AuthType.SIGN_UP);
  }
}
