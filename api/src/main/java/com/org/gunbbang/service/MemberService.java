package com.org.gunbbang.service;

import com.org.gunbbang.*;
import com.org.gunbbang.common.AuthType;
import com.org.gunbbang.controller.DTO.request.MemberSignUpRequestDTO;
import com.org.gunbbang.controller.DTO.request.MemberTypesRequestDTO;
import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.entity.BreadType;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.NutrientType;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.jwt.service.AppleJwtService;
import com.org.gunbbang.repository.BreadTypeRepository;
import com.org.gunbbang.repository.MemberRepository;
import com.org.gunbbang.repository.NutrientTypeRepository;
import com.org.gunbbang.util.mapper.BreadTypeMapper;
import com.org.gunbbang.util.mapper.MemberMapper;
import com.org.gunbbang.util.mapper.MemberTypeMapper;
import com.org.gunbbang.util.mapper.NutrientTypeMapper;
import com.org.gunbbang.util.security.SecurityUtil;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final BreadTypeRepository breadTypeRepository;
  private final NutrientTypeRepository nutrientTypeRepository;
  private final AppleJwtService appleJWTService;

  public MemberDetailResponseDTO getMemberDetail() {
    String memberNickname = SecurityUtil.getLoginMemberNickname();
    MainPurpose memberMainPurpose = SecurityUtil.getLoginMemberMainPurpose();
    Long memberBreadTypeId = SecurityUtil.getLoginMemberBreadTypeId();

    BreadType breadType =
        breadTypeRepository
            .findById(memberBreadTypeId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BREAD_TYPE_EXCEPTION));
    BreadTypeResponseDTO breadTypeResponseDTO =
        BreadTypeMapper.INSTANCE.toBreadTypeResponseDTO(breadType);

    return MemberTypeMapper.INSTANCE.toMemberDetailResponseDTO(
        memberNickname, memberMainPurpose, breadTypeResponseDTO);
  }

  public MemberSignUpResponseDTO signUp(MemberSignUpRequestDTO memberSignUpRequestDTO)
      throws Exception {

    if (memberRepository.findByEmail(memberSignUpRequestDTO.getEmail()).isPresent()) {
      throw new BadRequestException(ErrorType.ALREADY_EXIST_EMAIL_EXCEPTION);
    }

    if (memberRepository.findByNickname(memberSignUpRequestDTO.getNickname()).isPresent()) {
      throw new BadRequestException(ErrorType.ALREADY_EXIST_NICKNAME_EXCEPTION);
    }

    BreadType defaultBreadType =
        breadTypeRepository
            .findBreadTypeByIsGlutenFreeAndIsVeganAndIsNutFreeAndIsSugarFree(
                false, false, false, false)
            .orElseThrow();

    NutrientType defaultNutrientType =
        nutrientTypeRepository
            .findByIsNutrientOpenAndIsIngredientOpenAndIsNotOpen(false, false, false)
            .orElseThrow();

    Member member =
        MemberMapper.INSTANCE.toMemberEntity(
            memberSignUpRequestDTO, defaultBreadType, defaultNutrientType, Role.USER);
    member.passwordEncode(passwordEncoder);
    Member savedMember = memberRepository.saveAndFlush(member);

    return MemberSignUpResponseDTO.builder()
        .memberId(savedMember.getMemberId())
        .type(AuthType.SIGN_UP)
        .email(savedMember.getEmail())
        .build();
  }

  public MemberTypeResponseDTO updateMemberTypes(
      MemberTypesRequestDTO request, Long memberId, String nickname) {
    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));

    BreadType breadType =
        breadTypeRepository
            .findBreadTypeByIsGlutenFreeAndIsVeganAndIsNutFreeAndIsSugarFree(
                request.getBreadType().getIsGlutenFree(),
                request.getBreadType().getIsVegan(),
                request.getBreadType().getIsNutFree(),
                request.getBreadType().getIsSugarFree())
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BREAD_TYPE_EXCEPTION));

    NutrientType nutrientType =
        nutrientTypeRepository
            .findByIsNutrientOpenAndIsIngredientOpenAndIsNotOpen(
                request.getNutrientType().getIsNutrientOpen(),
                request.getNutrientType().getIsIngredientOpen(),
                request.getNutrientType().getIsNotOpen())
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_NUTRIENT_EXCEPTION));

    foundMember.updateBreadType(breadType);
    foundMember.updateNutrientType(nutrientType);
    foundMember.updateMainPurpose(request.getMainPurpose());
    memberRepository.saveAndFlush(foundMember);

    BreadTypeResponseDTO breadTypeResponseDTO =
        BreadTypeMapper.INSTANCE.toBreadTypeResponseDTO(foundMember.getBreadType());
    NutrientTypeResponseDTO nutrientTypeResponseDTO =
        NutrientTypeMapper.INSTANCE.toNutrientTypeResponseDTO(foundMember.getNutrientType());

    return MemberTypeMapper.INSTANCE.toMemberTypeResponseDTO(
        foundMember.getMemberId(),
        foundMember.getMainPurpose(),
        nickname,
        breadTypeResponseDTO,
        nutrientTypeResponseDTO);
  }

  public MemberTypeResponseDTO getMemberTypes(Map<String, Object> loginMemberInfo) {
    BreadType breadType =
        breadTypeRepository
            .findById(Long.parseLong(loginMemberInfo.get("breadTypeId").toString()))
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BREAD_TYPE_EXCEPTION));
    NutrientType nutrientType =
        nutrientTypeRepository
            .findById(Long.parseLong(loginMemberInfo.get("nutrientTypeId").toString()))
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_NUTRIENT_EXCEPTION));

    BreadTypeResponseDTO breadTypeResponseDTO =
        BreadTypeMapper.INSTANCE.toBreadTypeResponseDTO(breadType);
    NutrientTypeResponseDTO nutrientTypeResponseDTO =
        NutrientTypeMapper.INSTANCE.toNutrientTypeResponseDTO(nutrientType);

    return MemberTypeMapper.INSTANCE.toMemberTypeResponseDTO(
        Long.parseLong(loginMemberInfo.get("memberId").toString()),
        (MainPurpose) loginMemberInfo.get("mainPurpose"),
        loginMemberInfo.get("nickname").toString(),
        breadTypeResponseDTO,
        nutrientTypeResponseDTO);
  }

  public void checkDuplicatedNickname(String nickname) {
    if (memberRepository.findByNickname(nickname).isPresent()) {
      throw new BadRequestException(ErrorType.ALREADY_EXIST_NICKNAME_EXCEPTION);
    }
  }

  public void checkDuplicatedEmail(String email) {
    if (memberRepository.findByEmail(email).isPresent()) {
      throw new BadRequestException(ErrorType.ALREADY_EXIST_EMAIL_EXCEPTION);
    }
  }

  public MemberWithdrawResponseDTO withdraw(Long memberId, String appleRefreshToken)
      throws Exception {
    if (getMemberPlatformType(memberId) == PlatformType.APPLE) {
      appleJWTService.revokeAppleTokens(appleRefreshToken);
    }

    Long deletedMemberCount = memberRepository.deleteMemberByMemberId(memberId).get();

    if (deletedMemberCount == 0) {
      throw new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION);
    }

    SecurityContextHolder.clearContext();
    return MemberWithdrawResponseDTO.builder().memberId(memberId).build();
  }

  private PlatformType getMemberPlatformType(Long memberId) {
    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));
    return foundMember.getPlatformType();
  }

  public NicknameUpdateResponseDTO updateMemberNickname(Long memberId, String nickname) {
    if (memberRepository.findByNickname(nickname).isPresent()) {
      throw new BadRequestException(ErrorType.ALREADY_EXIST_NICKNAME_EXCEPTION);
    }
    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));

    foundMember.authorizeUser();
    foundMember.updateNickname(nickname);
    Member savedMember = memberRepository.saveAndFlush(foundMember);

    return NicknameUpdateResponseDTO.builder()
        .nickname(savedMember.getNickname())
        .role(savedMember.getRole())
        .memberId(savedMember.getMemberId())
        .build();
  }
}
