package com.org.gunbbang.service;

import com.org.gunbbang.BadRequestException;
import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.Role;
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

  public MemberSignUpResponseDTO signUp(MemberSignUpRequestDTO memberSignUpRequestDTO) {
    if (memberRepository.findByEmail(memberSignUpRequestDTO.getEmail()).isPresent()) {
      throw new BadRequestException(ErrorType.ALREADY_EXIST_EMAIL_EXCEPTION);
    }

    if (memberRepository.findByNickname(memberSignUpRequestDTO.getNickname()).isPresent()) {
      throw new BadRequestException(ErrorType.ALREADY_EXIST_NICKNAME_EXCEPTION);
    }

    BreadType breadType =
        breadTypeRepository
            .findBreadTypeByIsGlutenFreeAndIsVeganAndIsNutFreeAndIsSugarFree(
                memberSignUpRequestDTO.getBreadType().getIsGlutenFree(),
                memberSignUpRequestDTO.getBreadType().getIsVegan(),
                memberSignUpRequestDTO.getBreadType().getIsNutFree(),
                memberSignUpRequestDTO.getBreadType().getIsSugarFree())
            .orElseThrow();

    NutrientType nutrientType =
        nutrientTypeRepository
            .findByIsNutrientOpenAndIsIngredientOpenAndIsNotOpen(
                memberSignUpRequestDTO.getNutrientType().getIsNutrientOpen(),
                memberSignUpRequestDTO.getNutrientType().getIsIngredientOpen(),
                memberSignUpRequestDTO.getNutrientType().getIsNotOpen())
            .orElseThrow();

    Member member =
        Member.builder()
            .email(memberSignUpRequestDTO.getEmail().strip())
            .password(memberSignUpRequestDTO.getPassword().strip())
            .platformType(memberSignUpRequestDTO.getPlatformType())
            .nickname(memberSignUpRequestDTO.getNickname().strip())
            .role(Role.USER)
            .breadType(breadType)
            .nutrientType(nutrientType)
            .mainPurpose(MainPurpose.valueOf(memberSignUpRequestDTO.getMainPurpose()))
            .build();

    member.passwordEncode(passwordEncoder); // security에서 제공하는 PasswordEncoder로 유저의 비밀번호 인코딩해서 저장
    memberRepository.saveAndFlush(member);

    return MemberSignUpResponseDTO.builder()
        .type(AuthType.SIGN_UP)
        .email(member.getEmail())
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

  //  public MemberNicknameResponseDTO getMemberNickname(Long memberId) {
  //    Optional<String> nickname =
  //            memberRepository
  //                    .findNicknameById(memberId)
  //                    .orElseThrow();
  //    return
  //  }

}
