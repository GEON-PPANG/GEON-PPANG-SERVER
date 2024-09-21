package com.org.gunbbang.service;

import com.org.gunbbang.support.exception.BadRequestException;
import com.org.gunbbang.DTO.AppleAuthResponseDTO;
import com.org.gunbbang.auth.jwt.service.AppleJwtService;
import com.org.gunbbang.common.AuthType;
import com.org.gunbbang.controller.DTO.request.MemberSignUpRequestDTO;
import com.org.gunbbang.entity.*;
import com.org.gunbbang.support.errorType.ErrorType;
import com.org.gunbbang.repository.*;
import com.org.gunbbang.service.VO.SignedUpMemberVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AppleAuthService extends AuthService {
  private final AppleJwtService appleJwtService;

  public AppleAuthService(MemberRepository memberRepository, AppleJwtService appleJwtService) {
    super(memberRepository);
    this.appleJwtService = appleJwtService;
  }

  @Override
  public SignedUpMemberVO saveMemberOrLogin(String platformToken, MemberSignUpRequestDTO request)
      throws Exception {
    if (request.getEmail() == null || request.getEmail().isBlank()) {
      log.warn("@@@@@@@@@@ 애플 회원가입 시 email 필드가 없음 @@@@@@@@@@");
      throw new BadRequestException(
          ErrorType.NO_REQUEST_PARAMETER_EXCEPTION,
          ErrorType.NO_REQUEST_PARAMETER_EXCEPTION.getMessage()
              + " email 필드 누락됨: "
              + request.getEmail());
    }

    AppleAuthResponseDTO response = appleJwtService.validateAuthorizationCode(platformToken);

    Member foundMember = getUser(request.getPlatformType(), request.getEmail());
    if (foundMember != null) {
      return SignedUpMemberVO.of(foundMember, response.getRefresh_token(), AuthType.LOGIN);
    }

    Member savedMember = saveUser(request, request.getEmail());
    return SignedUpMemberVO.of(savedMember, response.getRefresh_token(), AuthType.SIGN_UP);
  }
}
