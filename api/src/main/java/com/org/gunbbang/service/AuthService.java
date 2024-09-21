package com.org.gunbbang.service;

import com.org.gunbbang.PlatformType;
import com.org.gunbbang.Role;
import com.org.gunbbang.controller.DTO.request.MemberSignUpRequestDTO;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.repository.*;
import com.org.gunbbang.service.VO.SignedUpMemberVO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Transactional
public abstract class AuthService {
  private final MemberRepository memberRepository;

  public AuthService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @Autowired private PasswordEncoder passwordEncoder;

  public abstract SignedUpMemberVO saveMemberOrLogin(
      String platformToken, MemberSignUpRequestDTO request) throws Exception;

  protected Member getUser(PlatformType platformType, String email) {
    return memberRepository.findByEmailAndPlatformType(email, platformType).orElse(null);
  }

  // OAuthAttributes의 toEntity() 메서드를 통해 빌터로 Member 객체 생성 후 반환한다
  // 생성된 Member 객체를 DB에 저장한다: socialType, email, role 값만 있는 상태이다
  protected Member saveUser(MemberSignUpRequestDTO request, String email) {

    // 소셜 로그인일 때
    if (request.getPlatformType() != PlatformType.NONE) {
      Member newMember = createSocialMember(request, email);
      return memberRepository.saveAndFlush(newMember);
    }

    // 자체로그인일 때
    Member newMember = createNativeMember(request, email);
    return memberRepository.saveAndFlush(newMember);
  }

  private Member createNativeMember(MemberSignUpRequestDTO request, String email) {
    final String encodedPassword = passwordEncoder.encode(request.getPassword());

    return Member.builder()
        .platformType(request.getPlatformType())
        .email(email)
        .nickname(request.getNickname())   // 자체로그인 시 request에 있는 nickName
        .role(Role.ROLE_MEMBER)            // 자체로그인 시 MEMBER
        .password(encodedPassword)         // 자체로그인 시 password 저장해야함
        .build();
  }

  private Member createSocialMember(MemberSignUpRequestDTO request, String email) {
    return Member.builder()
        .platformType(request.getPlatformType())
        .email(email)
        .nickname(Role.ROLE_GUEST.name())
        .role(Role.ROLE_GUEST)
        .build();
  }
}
