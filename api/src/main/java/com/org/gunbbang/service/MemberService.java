package com.org.gunbbang.service;

import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.controller.DTO.response.BreadTypeResponseDto;
import com.org.gunbbang.controller.DTO.response.MemberDetailResponseDto;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.org.gunbbang.Role;
import com.org.gunbbang.controller.DTO.MemberSignUpRequestDTO;
import com.org.gunbbang.controller.DTO.MemberSignUpResponseDTO;
import com.org.gunbbang.entity.BreadType;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.NutrientType;

import com.org.gunbbang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberDetailResponseDto getMemberDetail(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));
        BreadTypeResponseDto breadTypeResponseDto = BreadTypeResponseDto.builder()
                .breadTypeId(member.getBreadTypeId().getBreadTypeId())
                .breadTypeName(member.getBreadTypeId().getBreadTypeName())
                .isVegan(member.getBreadTypeId().isVegan())
                .isGlutenFree(member.getBreadTypeId().isGlutenFree())
                .isSugarFree(member.getBreadTypeId().isSugarFree())
                .isNutFree(member.getBreadTypeId().isNutFree())
                .build();

        return MemberDetailResponseDto.builder()
                .memberNickname(member.getNickname())
                .mainPurpose(member.getMainPurpose())
                .breadType(breadTypeResponseDto)
                .build();
    }

    public MemberSignUpResponseDTO signUp(MemberSignUpRequestDTO memberSignUpRequestDTO) throws Exception {
        if (memberRepository.findByEmail(memberSignUpRequestDTO.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if (memberRepository.findByNickname(memberSignUpRequestDTO.getNickName()).isPresent()) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        /**
         * TODO: 이거 추가되어야 함
         * 클라에서는 아래처럼 보내줄거임
         *     isGlutenFree: true
         *     isVegan: false
         *     isNutFree: false
         *     isSugarFree: ture
         *
         * 그럼 보내준 이 응답을 바탕으로 repository에서 조회
         * -> 조회해서 가져온 id로 저장
         */
        Member member = Member.builder()
                .email(memberSignUpRequestDTO.getEmail())
                .password(memberSignUpRequestDTO.getPassword())
                .platformType(memberSignUpRequestDTO.getPlatformType())
                .nickname(memberSignUpRequestDTO.getNickName())
                .role(Role.USER)
//                .breadTypeId() // 수정 필요
//                .nutrientTypeId()
                .mainPurpose(memberSignUpRequestDTO.getMainPurpose())
                .build();

        member.passwordEncode(passwordEncoder);
        memberRepository.save(member);

        return new MemberSignUpResponseDTO(
            "signup",
            member.getEmail()
        );

    }
}
