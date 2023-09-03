package com.org.gunbbang.service;

import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.PlatformType;
import com.org.gunbbang.Role;
import com.org.gunbbang.controller.DTO.request.MemberSignUpRequestDTO;
import com.org.gunbbang.entity.BreadType;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.NutrientType;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.BreadTypeRepository;
import com.org.gunbbang.repository.MemberRepository;
import com.org.gunbbang.repository.NutrientTypeRepository;
import com.org.gunbbang.service.VO.SignedUpMemberVO;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class AuthService {
  private final MemberRepository memberRepository;
  private final BreadTypeRepository breadTypeRepository;
  private final NutrientTypeRepository nutrientTypeRepository;

  public AuthService(
      MemberRepository memberRepository,
      BreadTypeRepository breadTypeRepository,
      NutrientTypeRepository nutrientTypeRepository) {
    this.memberRepository = memberRepository;
    this.breadTypeRepository = breadTypeRepository;
    this.nutrientTypeRepository = nutrientTypeRepository;
  }

  @Autowired private PasswordEncoder passwordEncoder; // TODO: final 빼고 하는게 맞을지?

  public abstract SignedUpMemberVO saveMemberOrLogin(
      String platformToken, MemberSignUpRequestDTO request) throws Exception;

  protected Member getUser(PlatformType platformType, String email) {
    return memberRepository.findByPlatformTypeAndEmail(platformType, email).orElse(null);
  }

  // OAuthAttributes의 toEntity() 메서드를 통해 빌터로 Member 객체 생성 후 반환한다
  // 생성된 Member 객체를 DB에 저장한다: socialType, email, role 값만 있는 상태이다
  protected Member saveUser(MemberSignUpRequestDTO request, String email) {
    BreadType defaultBreadType =
        breadTypeRepository
            .findBreadTypeByIsGlutenFreeAndIsVeganAndIsNutFreeAndIsSugarFree(
                false, false, false, false)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BREAD_TYPE_EXCEPTION));

    NutrientType defaultNutrientType =
        nutrientTypeRepository
            .findByIsNutrientOpenAndIsIngredientOpenAndIsNotOpen(false, false, false)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_NUTRIENT_EXCEPTION));

    // 소셜 로그인일 때
    if (request.getPlatformType() != PlatformType.NONE) {
      Member newMember = createSocialMember(request, email, defaultBreadType, defaultNutrientType);
      return memberRepository.saveAndFlush(newMember);
    }

    // 자체로그인일 때
    Member newMember = createNativeMember(request, email, defaultBreadType, defaultNutrientType);
    newMember.passwordEncode(passwordEncoder);
    return memberRepository.saveAndFlush(newMember);
  }

  private static Member createNativeMember(
      MemberSignUpRequestDTO request,
      String email,
      BreadType defaultBreadType,
      NutrientType defaultNutrientType) {
    return Member.builder()
        .platformType(request.getPlatformType())
        .email(email)
        .nickname(request.getNickname()) // 자체로그인 시 request에 있는 nickName
        .role(Role.USER) // 자체로그인 시 USER
        .breadType(defaultBreadType)
        .nutrientType(defaultNutrientType)
        .password(request.getPassword()) // 자체로그인 시 password 저장해야함
        .build();
  }

  private static Member createSocialMember(
      MemberSignUpRequestDTO request,
      String email,
      BreadType defaultBreadType,
      NutrientType defaultNutrientType) {
    return Member.builder()
        .platformType(request.getPlatformType())
        .email(email)
        .nickname(Role.GUEST.name() + " " + UUID.randomUUID())
        .role(Role.GUEST)
        .breadType(defaultBreadType)
        .nutrientType(defaultNutrientType)
        .build();
  }
}
