package com.org.gunbbang.service;

import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.auth.security.util.SecurityUtil;
import com.org.gunbbang.controller.DTO.request.MemberTypesRequestDTO;
import com.org.gunbbang.controller.DTO.response.BreadTypeResponseDTO;
import com.org.gunbbang.controller.DTO.response.MemberDetailResponseDTO;
import com.org.gunbbang.controller.DTO.response.MemberTypeResponseDTO;
import com.org.gunbbang.controller.DTO.response.NutrientTypeResponseDTO;
import com.org.gunbbang.entity.*;
import com.org.gunbbang.repository.*;
import com.org.gunbbang.support.errorType.ErrorType;
import com.org.gunbbang.support.exception.NotFoundException;
import com.org.gunbbang.util.mapper.MemberBreadTypeMapper;
import com.org.gunbbang.util.mapper.MemberNutrientTypeMapper;
import com.org.gunbbang.util.mapper.MemberTypeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberTypeService {

  private final MemberRepository memberRepository;
  private final BreadTypeRepository breadTypeRepository;
  private final NutrientTypeRepository nutrientTypeRepository;
  private final MemberBreadTypeRepository memberBreadTypeRepository;
  private final MemberNutrientTypeRepository memberNutrientTypeRepository;

  @Transactional(readOnly = true)
  public MemberDetailResponseDTO getMemberDetail() {
    String memberNickname = SecurityUtil.getLoginMemberNickname();
    MainPurpose memberMainPurpose = SecurityUtil.getLoginMemberMainPurpose();

    Member foundMember =
            memberRepository
                    .findByNickname(memberNickname)
                    .orElseThrow(
                            () ->
                                    new NotFoundException(
                                            ErrorType.NOT_FOUND_USER_EXCEPTION,
                                            ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberNickname));

    List<MemberBreadType> breadType = memberBreadTypeRepository.findAllByMember(foundMember);
    List<BreadTypeResponseDTO> breadTypeResponseDTO =
            MemberBreadTypeMapper.INSTANCE.toBreadTypeResponseDTOList(breadType);

    return MemberTypeMapper.INSTANCE.toMemberDetailResponseDTO(
            memberNickname, memberMainPurpose, breadTypeResponseDTO);
  }

  @Transactional
  public MemberTypeResponseDTO updateMemberTypes(
          MemberTypesRequestDTO request, Long memberId, String nickname) {
    Member foundMember =
            memberRepository
                    .findById(memberId)
                    .orElseThrow(
                            () ->
                                    new NotFoundException(
                                            ErrorType.NOT_FOUND_USER_EXCEPTION,
                                            ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId));

    if (memberBreadTypeRepository.existsByMember(foundMember)) {
      memberBreadTypeRepository.deleteAllByMember(foundMember); // 기존에 있던 MemberBreadType 전부 삭제
    }

    List<MemberBreadType> memberBreadTypes =
            saveBreadTypes(request.getBreadTypeList(), foundMember);

    if (memberNutrientTypeRepository.existsByMember(foundMember)) {
      memberNutrientTypeRepository.deleteAllByMember(foundMember); // 기존에 있던 MemberNutrientType 전부 삭제
    }

    List<MemberNutrientType> memberNutrientTypes =
            saveNutrientTypes(request.getNutrientTypeList(), foundMember);

    foundMember.updateMainPurpose(request.getMainPurpose());
    memberRepository.saveAndFlush(foundMember);

    List<BreadTypeResponseDTO> breadTypeResponseDTO =
            MemberBreadTypeMapper.INSTANCE.toBreadTypeResponseDTOList(memberBreadTypes);
    List<NutrientTypeResponseDTO> nutrientTypeResponseDTO =
            MemberNutrientTypeMapper.INSTANCE.toNutrientTypeResponseDTOList(memberNutrientTypes);

    return MemberTypeMapper.INSTANCE.toMemberTypeResponseDTO(
            foundMember.getMemberId(),
            foundMember.getMainPurpose(),
            nickname,
            breadTypeResponseDTO,
            nutrientTypeResponseDTO);
  }

  private List<MemberBreadType> saveBreadTypes(List<Long> breadTypeIds, Member foundMember) {
    List<MemberBreadType> memberBreadTypes = new ArrayList<>();

    for (Long breadTypeId : breadTypeIds) {
      memberBreadTypes.add(createMemberBreadTypeById(foundMember, breadTypeId));
    }
    return memberBreadTypeRepository.saveAllAndFlush(memberBreadTypes);
  }

  private MemberBreadType createMemberBreadTypeById(Member foundMember, Long breadTypeId) {
    BreadType foundBreadType =
            breadTypeRepository
                    .findByBreadTypeId(breadTypeId)
                    .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BREAD_TYPE_EXCEPTION));

    MemberBreadType memberBreadType =
            MemberBreadType.builder().member(foundMember).breadType(foundBreadType).build();
    return memberBreadType;
  }

  private List<MemberNutrientType> saveNutrientTypes(
          List<Long> nutrientTypeIds, Member foundMember) {
    List<MemberNutrientType> memberNutrientTypes = new ArrayList<>();

    for (Long nutrientTypeId : nutrientTypeIds) {
      memberNutrientTypes.add(createMemberNutrientTypeById(foundMember, nutrientTypeId));
    }

    return memberNutrientTypeRepository.saveAllAndFlush(memberNutrientTypes);
  }

  private MemberNutrientType createMemberNutrientTypeById(Member foundMember, Long nutrientTypeId) {
    NutrientType foundNutrientType =
            nutrientTypeRepository
                    .findByNutrientTypeId(nutrientTypeId)
                    .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_NUTRIENT_EXCEPTION));

    MemberNutrientType memberNutrientType =
            MemberNutrientType.builder().member(foundMember).nutrientType(foundNutrientType).build();
    return memberNutrientType;
  }

  @Transactional(readOnly = true)
  public MemberTypeResponseDTO getMemberTypes(Map<String, Object> loginMemberInfo) {
    Long memberId = Long.parseLong(loginMemberInfo.get("memberId").toString());
    Member member =
            memberRepository
                    .findById(memberId)
                    .orElseThrow(
                            () ->
                                    new NotFoundException(
                                            ErrorType.NOT_FOUND_USER_EXCEPTION,
                                            ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId));
    List<MemberBreadType> memberBreadTypes = memberBreadTypeRepository.findAllByMember(member);
    List<MemberNutrientType> memberNutrientTypes =
            memberNutrientTypeRepository.findAllByMemberId(memberId);

    List<BreadTypeResponseDTO> breadTypeResponseDTO =
            MemberBreadTypeMapper.INSTANCE.toBreadTypeResponseDTOList(memberBreadTypes);
    List<NutrientTypeResponseDTO> nutrientTypeResponseDTO =
            MemberNutrientTypeMapper.INSTANCE.toNutrientTypeResponseDTOList(memberNutrientTypes);

    return MemberTypeMapper.INSTANCE.toMemberTypeResponseDTO(
            memberId,
            (MainPurpose) loginMemberInfo.get("mainPurpose"),
            loginMemberInfo.get("nickname").toString(),
            breadTypeResponseDTO,
            nutrientTypeResponseDTO);
  }
}
