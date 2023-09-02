package com.org.gunbbang.login.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.PlatformType;
import com.org.gunbbang.Role;
import com.org.gunbbang.entity.BreadType;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.NutrientType;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.BreadTypeRepository;
import com.org.gunbbang.repository.MemberRepository;
import com.org.gunbbang.repository.NutrientTypeRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AuthService {
  private final MemberRepository memberRepository;
  private final BreadTypeRepository breadTypeRepository;
  private final NutrientTypeRepository nutrientTypeRepository;

  public abstract Member loadMemberByToken(String accessToken, PlatformType platformType)
      throws JsonProcessingException;

  // SocialType과 Email을 통해 회원가입한 회원을 찾고 없으면 가입시킨다
  // TODO: Email은 같으나 SocialType이 다르게 들어온 요청의 경우 어떻게 처리해야할 지 고민해야한다
  protected Member getUser(PlatformType platformType, String email) {
    return memberRepository
        .findByPlatformTypeAndEmail(platformType, email) // 있으면 찾고
        .orElse(saveUser(platformType, email)); // 없으면 가입시킨다
  }

  // OAuthAttributes의 toEntity() 메서드를 통해 빌터로 Member 객체 생성 후 반환한다
  // 생성된 Member 객체를 DB에 저장한다: socialType, email, role 값만 있는 상태이다
  private Member saveUser(PlatformType platformType, String email) {
    BreadType defaultBreadType =
        breadTypeRepository
            .findBreadTypeByIsGlutenFreeAndIsVeganAndIsNutFreeAndIsSugarFree(
                false, false, false, false)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BREAD_TYPE_EXCEPTION));

    NutrientType defaultNutrientType =
        nutrientTypeRepository
            .findByIsNutrientOpenAndIsIngredientOpenAndIsNotOpen(false, false, false)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_NUTRIENT_EXCEPTION));

    Member createdUser =
        Member.builder()
            .platformType(platformType)
            .email(email)
            .nickname("GUEST")
            .role(Role.GUEST)
            .breadType(defaultBreadType)
            .nutrientType(defaultNutrientType)
            .build();
    return memberRepository.save(createdUser);
  }
}
