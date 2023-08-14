package com.org.gunbbang.service;

import com.org.gunbbang.BadRequestException;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.common.AuthType;
import com.org.gunbbang.controller.DTO.request.MemberSignUpRequestDTO;
import com.org.gunbbang.controller.DTO.request.MemberTypesRequestDTO;
import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.entity.BreadType;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.NutrientType;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.BreadTypeRepository;
import com.org.gunbbang.repository.MemberRepository;
import com.org.gunbbang.repository.NutrientTypeRepository;
import com.org.gunbbang.util.mapper.BreadTypeMapper;
import com.org.gunbbang.util.mapper.MemberMapper;
import com.org.gunbbang.util.mapper.MemberTypeMapper;
import com.org.gunbbang.util.mapper.NutrientTypeMapper;
import com.org.gunbbang.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  public MemberDetailResponseDTO getMemberDetail() {
    Long currentMemberId = SecurityUtil.getLoginMemberId();

    Member member =
        memberRepository
            .findById(currentMemberId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));
    BreadTypeResponseDTO breadType =
        BreadTypeResponseDTO.builder()
            .breadTypeId(member.getBreadType().getBreadTypeId())
            .breadTypeName(member.getBreadType().getBreadTypeName())
            .isVegan(member.getBreadType().getIsVegan())
            .isGlutenFree(member.getBreadType().getIsGlutenFree())
            .isSugarFree(member.getBreadType().getIsSugarFree())
            .isNutFree(member.getBreadType().getIsNutFree())
            .build();

    return MemberDetailResponseDTO.builder()
        .memberNickname(member.getNickname())
        .mainPurpose(member.getMainPurpose())
        .breadType(breadType)
        .build();
  }

  public MemberSignUpResponseDTO signUp(MemberSignUpRequestDTO memberSignUpRequestDTO) {
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
            memberSignUpRequestDTO, defaultBreadType, defaultNutrientType);
    member.passwordEncode(passwordEncoder);
    Member savedMember = memberRepository.saveAndFlush(member);

    return MemberSignUpResponseDTO.builder()
        .memberId(savedMember.getMemberId())
        .type(AuthType.SIGN_UP)
        .email(savedMember.getEmail())
        .build();
  }

  public MemberTypeResponseDTO updateMemberTypes(MemberTypesRequestDTO request, Long memberId) {
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
        breadTypeResponseDTO,
        nutrientTypeResponseDTO);
  }

  public MemberTypeResponseDTO getMemberTypes(Long memberId) {
    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));

    BreadTypeResponseDTO breadTypeResponseDTO =
        BreadTypeMapper.INSTANCE.toBreadTypeResponseDTO(foundMember.getBreadType());
    NutrientTypeResponseDTO nutrientTypeResponseDTO =
        NutrientTypeMapper.INSTANCE.toNutrientTypeResponseDTO(foundMember.getNutrientType());

    return MemberTypeMapper.INSTANCE.toMemberTypeResponseDTO(
        foundMember.getMemberId(),
        foundMember.getMainPurpose(),
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
}
