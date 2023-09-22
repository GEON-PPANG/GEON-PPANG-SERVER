package com.org.gunbbang.service;

import com.org.gunbbang.BadRequestException;
import com.org.gunbbang.DTO.AppleAuthResponseDTO;
import com.org.gunbbang.common.AuthType;
import com.org.gunbbang.controller.DTO.request.MemberSignUpRequestDTO;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.jwt.service.AppleJwtService;
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
