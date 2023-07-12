package com.org.gunbbang.service;

import com.org.gunbbang.BadRequestException;
import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.common.AuthType;
import com.org.gunbbang.controller.DTO.request.MemberTypesRequestDTO;
import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.controller.VO.CurrentMemberVO;
import com.org.gunbbang.entity.BreadType;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.NutrientType;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.BreadTypeRepository;
import com.org.gunbbang.repository.MemberRepository;
import com.org.gunbbang.repository.NutrientTypeRepository;
import com.org.gunbbang.util.Security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.gunbbang.Role;
import com.org.gunbbang.controller.DTO.request.MemberSignUpRequestDTO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final BreadTypeRepository breadTypeRepository;
    private final NutrientTypeRepository nutrientTypeRepository;

    public MemberDetailResponseDto getMemberDetail() {
        Long currentMemberId = SecurityUtil.getLoginMemberId();

        Member member = memberRepository.findById(currentMemberId).orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));
        BreadTypeResponseDto breadTypeResponseDto = BreadTypeResponseDto.builder()
                .breadTypeId(member.getBreadType().getBreadTypeId())
                .breadTypeName(member.getBreadType().getBreadTypeName())
                .isVegan(member.getBreadType().getIsVegan())
                .isGlutenFree(member.getBreadType().getIsGlutenFree())
                .isSugarFree(member.getBreadType().getIsSugarFree())
                .isNutFree(member.getBreadType().getIsNutFree())
                .build();

        return MemberDetailResponseDto.builder()
                .memberNickname(member.getNickname())
                .mainPurpose(member.getMainPurpose())
                .breadType(breadTypeResponseDto)
                .build();
    }

    public MemberSignUpResponseDTO signUp(MemberSignUpRequestDTO memberSignUpRequestDTO) {
        if (memberRepository.findByEmail(memberSignUpRequestDTO.getEmail()).isPresent()) {
            throw new BadRequestException(ErrorType.ALREADY_EXIST_EMAIL_EXCEPTION);
        }

        if (memberRepository.findByNickname(memberSignUpRequestDTO.getNickname()).isPresent()) {
            throw new BadRequestException(ErrorType.ALREADY_EXIST_NICKNAME_EXCEPTION);
        }

        BreadType breadType = breadTypeRepository.findBreadTypeByIsGlutenFreeAndIsVeganAndIsNutFreeAndIsSugarFree(
                memberSignUpRequestDTO.getBreadType().getIsGlutenFree(),
                memberSignUpRequestDTO.getBreadType().getIsVegan(),
                memberSignUpRequestDTO.getBreadType().getIsNutFree(),
                memberSignUpRequestDTO.getBreadType().getIsSugarFree()
        ).orElseThrow();

        NutrientType nutrientType = nutrientTypeRepository.findByIsNutrientOpenAndIsIngredientOpenAndIsNotOpen(
                memberSignUpRequestDTO.getNutrientType().getIsNutrientOpen(),
                memberSignUpRequestDTO.getNutrientType().getIsIngredientOpen(),
                memberSignUpRequestDTO.getNutrientType().getIsNotOpen()
        ).orElseThrow();

        Member member = Member.builder()
                .email(memberSignUpRequestDTO.getEmail())
                .password(memberSignUpRequestDTO.getPassword())
                .platformType(memberSignUpRequestDTO.getPlatformType())
                .nickname(memberSignUpRequestDTO.getNickname())
                .role(Role.USER)
                .breadType(breadType)
                .nutrientType(nutrientType)
                .mainPurpose(MainPurpose.valueOf(memberSignUpRequestDTO.getMainPurpose()))
                .build();

        member.passwordEncode(passwordEncoder);
        memberRepository.save(member);

        return MemberSignUpResponseDTO.builder()
                .type(AuthType.SIGN_UP)
                .email(member.getEmail())
                .build();
    }

    public CurrentMemberVO getCurrentMemberVO() {
        Long memberId = SecurityUtil.getLoginMemberId();
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));

        return CurrentMemberVO.of(
                foundMember.getMemberId(),
                foundMember.getEmail(),
                foundMember.getPassword(),
                foundMember.getNickname(),
                foundMember.getMainPurpose()
        );
    }

    public MemberTypesResponseDTO updateMemberTypes(MemberTypesRequestDTO request, Long memberId) {
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));

        BreadType breadType = breadTypeRepository.findBreadTypeByIsGlutenFreeAndIsVeganAndIsNutFreeAndIsSugarFree(
                request.getBreadType().getIsGlutenFree(),
                request.getBreadType().getIsVegan(),
                request.getBreadType().getIsNutFree(),
                request.getBreadType().getIsSugarFree()
        ).orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BREAD_TYPE_EXCEPTION));

        NutrientType nutrientType = nutrientTypeRepository.findByIsNutrientOpenAndIsIngredientOpenAndIsNotOpen(
                request.getNutrientType().getIsNutrientOpen(),
                request.getNutrientType().getIsIngredientOpen(),
                request.getNutrientType().getIsNotOpen()
        ).orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_NUTRIENT_EXCEPTION));

        foundMember.updateBreadType(breadType);
        foundMember.updateNutrientType(nutrientType);
        memberRepository.saveAndFlush(foundMember);

        // TODO: 이거 구체적으로 어떻게 돌아가는건지???
        BreadType foundMemberBreadType = foundMember.getBreadType();
        BreadTypeResponseDto breadTypeResponse = BreadTypeResponseDto.builder()
                .breadTypeId(foundMemberBreadType.getBreadTypeId())
                .breadTypeName(foundMemberBreadType.getBreadTypeName())
                .isGlutenFree(foundMemberBreadType.getIsGlutenFree())
                .isVegan(foundMemberBreadType.getIsVegan())
                .isNutFree(foundMemberBreadType.getIsNutFree())
                .isSugarFree(foundMemberBreadType.getIsSugarFree())
                .build();

        NutrientType foundMemberNutrientType = foundMember.getNutrientType();
        NutrientTypeResponseDTO nutrientTypeResponse = NutrientTypeResponseDTO.builder()
                .nutrientTypeId(foundMemberNutrientType.getNutrientTypeId())
                .nutrientTypeName(foundMemberNutrientType.getNutrientTypeName())
                .isNutrientOpen(foundMemberNutrientType.getIsNutrientOpen())
                .isIngredientOpen(foundMemberNutrientType.getIsIngredientOpen())
                .isNotOpen(foundMemberNutrientType.getIsNotOpen())
                .build();

        return MemberTypesResponseDTO.builder()
                .memberId(foundMember.getMemberId())
                .mainPurpose(foundMember.getMainPurpose())
                .breadType(breadTypeResponse)
                .nutrientType(nutrientTypeResponse)
                .build();
    }

    public MemberTypesResponseDTO getMemberTypes(Long memberId) {
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));

        BreadType foundMemberBreadType = foundMember.getBreadType();
        BreadTypeResponseDto breadTypeResponse = BreadTypeResponseDto.builder()
                .breadTypeId(foundMemberBreadType.getBreadTypeId())
                .breadTypeName(foundMemberBreadType.getBreadTypeName())
                .isGlutenFree(foundMemberBreadType.getIsGlutenFree())
                .isVegan(foundMemberBreadType.getIsVegan())
                .isNutFree(foundMemberBreadType.getIsNutFree())
                .isSugarFree(foundMemberBreadType.getIsSugarFree())
                .build();

        NutrientType foundMemberNutrientType = foundMember.getNutrientType();
        NutrientTypeResponseDTO nutrientTypeResponse = NutrientTypeResponseDTO.builder()
                .nutrientTypeId(foundMemberNutrientType.getNutrientTypeId())
                .nutrientTypeName(foundMemberNutrientType.getNutrientTypeName())
                .isNutrientOpen(foundMemberNutrientType.getIsNutrientOpen())
                .isIngredientOpen(foundMemberNutrientType.getIsIngredientOpen())
                .isNotOpen(foundMemberNutrientType.getIsNotOpen())
                .build();

        return MemberTypesResponseDTO.builder()
                .memberId(foundMember.getMemberId())
                .mainPurpose(foundMember.getMainPurpose())
                .breadType(breadTypeResponse)
                .nutrientType(nutrientTypeResponse)
                .build();
    }
}
