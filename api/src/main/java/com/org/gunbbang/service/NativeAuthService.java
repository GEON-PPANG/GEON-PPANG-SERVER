package com.org.gunbbang.service;

import com.org.gunbbang.BadRequestException;
import com.org.gunbbang.common.AuthType;
import com.org.gunbbang.controller.DTO.request.MemberSignUpRequestDTO;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.*;
import com.org.gunbbang.service.VO.SignedUpMemberVO;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NativeAuthService extends AuthService {

  private final MemberRepository memberRepository;

  public NativeAuthService(MemberRepository memberRepository) {
    super(memberRepository);
    this.memberRepository = memberRepository;
  }

  // 자체로그인 시 로그인 로직은 시큐리티에서 처리
  @Override
  @Transactional
  public SignedUpMemberVO saveMemberOrLogin(String platformToken, MemberSignUpRequestDTO request) {
    if (isNullOrBlank(request.getPassword())
        || isNullOrBlank(request.getNickname())
        || isNullOrBlank(request.getEmail())) {
      log.error(
          "%%%%%%%%%% 자체로그인 시 필요한 요청 값이 오지 않음. pwd: {} nickname: {} email: {} %%%%%%%%%%",
          request.getPassword(), request.getNickname(), request.getEmail());
      throw new BadRequestException(
          ErrorType.NO_REQUEST_PARAMETER_EXCEPTION,
          ErrorType.NO_REQUEST_PARAMETER_EXCEPTION.getMessage()
              + " email 필드 누락됨: "
              + request.getEmail());
    }

    // 자체로그인에서만 email 중복 비허용
    if (memberRepository
        .findByEmailAndPlatformType(request.getEmail(), request.getPlatformType())
        .isPresent()) {
      throw new BadRequestException(ErrorType.ALREADY_EXIST_EMAIL_EXCEPTION);
    }

    if (memberRepository.findByNickname(request.getNickname()).isPresent()) {
      throw new BadRequestException(ErrorType.ALREADY_EXIST_NICKNAME_EXCEPTION);
    }

    Member savedMember = saveUser(request, request.getEmail());
    return SignedUpMemberVO.of(savedMember, null, AuthType.LOGIN);
  }

  private boolean isNullOrBlank(String input) {
    return input == null || input.isBlank();
  }
}
