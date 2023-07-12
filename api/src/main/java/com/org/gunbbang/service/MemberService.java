package com.org.gunbbang.service;

import com.org.gunbbang.BadRequestException;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.common.AuthType;
import com.org.gunbbang.controller.DTO.response.BreadTypeResponseDto;
import com.org.gunbbang.controller.DTO.response.MemberDetailResponseDto;
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
import com.org.gunbbang.controller.DTO.response.MemberSignUpResponseDTO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

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
                .breadTypeId(member.getBreadTypeId().getBreadTypeId())
                .breadTypeName(member.getBreadTypeId().getBreadTypeName())
                .isVegan(member.getBreadTypeId().getIsVegan())
                .isGlutenFree(member.getBreadTypeId().getIsGlutenFree())
                .isSugarFree(member.getBreadTypeId().getIsSugarFree())
                .isNutFree(member.getBreadTypeId().getIsNutFree())
                .build();

        return MemberDetailResponseDto.builder()
                .memberNickname(member.getNickname())
                .mainPurpose(member.getMainPurpose())
                .breadType(breadTypeResponseDto)
                .build();
    }

    @Transactional
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
                .breadTypeId(breadType)
                .nutrientTypeId(nutrientType)
                .mainPurpose(memberSignUpRequestDTO.getMainPurpose())
                .build();

        member.passwordEncode(passwordEncoder);
        memberRepository.save(member);

        return MemberSignUpResponseDTO.builder()
                .type(AuthType.SIGN_UP)
                .email(member.getEmail())
                .build();
    }
}
