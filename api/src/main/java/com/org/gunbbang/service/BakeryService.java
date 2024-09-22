package com.org.gunbbang.service;

import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.support.exception.NotFoundException;
import com.org.gunbbang.auth.security.util.SecurityUtil;
import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.entity.*;
import com.org.gunbbang.support.errorType.ErrorType;
import com.org.gunbbang.repository.*;
import com.org.gunbbang.service.specification.BakerySpecifications;
import com.org.gunbbang.util.mapper.BakeryBreadTypeMapper;
import com.org.gunbbang.util.mapper.BakeryMapper;
import com.org.gunbbang.util.mapper.MenuMapper;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BakeryService {
  private final BookMarkRepository bookMarkRepository;
  private final MemberRepository memberRepository;
  private final BakeryRepository bakeryRepository;
  private final MenuRepository menuRepository;
  private final MemberBreadTypeRepository memberBreadTypeRepository;
  private final MemberNutrientTypeRepository memberNutrientTypeRepository;
  private final BakeryBreadTypeRepository bakeryBreadTypeRepository;

  private final String BLANK_SPACE = " ";
  private final int MAX_BEST_BAKERY_COUNT = 10;

  public BakeryDetailResponseDTO getBakeryDetail(Long bakeryId) {
    final Bakery bakery =
        bakeryRepository
            .findById(bakeryId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_BAKERY_EXCEPTION,
                        ErrorType.NOT_FOUND_BAKERY_EXCEPTION.getMessage() + bakeryId));
    final List<BreadTypeResponseDTO> breadType =
        BakeryBreadTypeMapper.INSTANCE.toBreadTypeResponseDTOList(
            bakeryBreadTypeRepository.findAllByBakery(bakery));
    final List<Menu> bakeryMenuList = menuRepository.findAllByBakery(bakery);
    final List<MenuResponseDTO> menuList =
        MenuMapper.INSTANCE.toMenuResponseDTOList(bakeryMenuList);
    final String address =
        getAddress(bakery.getState(), bakery.getCity(), bakery.getTown(), bakery.getAddressRest());
    final boolean isBookMarked =
        !SecurityUtil.checkAnonymousUser()
            && isBookMarked(SecurityUtil.getUserId().orElse(null), bakeryId);

    return BakeryMapper.INSTANCE.toBakeryDetailResponseDTO(
        bakery, address, breadType, isBookMarked, menuList);
  }

  String getAddress(String state, String city, String town, String addressRest) {
    return state + BLANK_SPACE + city + BLANK_SPACE + town + BLANK_SPACE + addressRest;
  }

  public List<BestBakeryListResponseDTO>
  getBestBakeries() {
    Optional<Long> memberId = SecurityUtil.getUserId();

    if (memberId.isEmpty()) {
      List<Bakery> randomBakeries = getOnlyRandomBakeries();
      return BakeryMapper.INSTANCE.toBestBakeryListResponseDTO(randomBakeries);
    }

    Member foundMember =
        memberRepository
            .findById(memberId.get())
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_USER_EXCEPTION,
                        ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId));

    if (!isFilterSelected(foundMember)) {
      log.info("########## 회원이 필터 선택 안한 경우. 랜덤으로 10개 건빵집 반환 ##########");
      List<Bakery> randomBakeries = getOnlyRandomBakeries();
      return BakeryMapper.INSTANCE.toBestBakeryListResponseDTO(randomBakeries);
    }

    List<BreadType> breadTypes =
        memberBreadTypeRepository.findAllByMember(foundMember).stream()
            .map(MemberBreadType::getBreadType)
            .collect(Collectors.toList());

    List<Bakery> bestBakeries = getBestBakeries(foundMember, breadTypes);
    if (bestBakeries.size() == MAX_BEST_BAKERY_COUNT) {
      log.info("########## 베스트 베이커리 10개 조회 완료. 추가 조회 쿼리 없이 바로 반환 ##########");
      return BakeryMapper.INSTANCE.toBestBakeryListResponseDTO(bestBakeries);
    }

    List<Long> alreadyFoundBakeryIds = new ArrayList<>();
    alreadyFoundBakeryIds.add(-1L);

    getRestBakeries(alreadyFoundBakeryIds, breadTypes, bestBakeries);
    if (bestBakeries.size() == MAX_BEST_BAKERY_COUNT) {
      log.info("##########빵유형 10개 조회 완료. 추가 조회 쿼리 없이 바로 반환 ##########");
      return BakeryMapper.INSTANCE.toBestBakeryListResponseDTO(bestBakeries);
    }

    getRestRandomBakeries(alreadyFoundBakeryIds, bestBakeries);
    return BakeryMapper.INSTANCE.toBestBakeryListResponseDTO(bestBakeries);
  }

  private List<Bakery> getOnlyRandomBakeries() {
    PageRequest bestPageRequest = PageRequest.of(0, MAX_BEST_BAKERY_COUNT);
    return bakeryRepository.findBakeriesRandomly(bestPageRequest);
  }

  private boolean isFilterSelected(Member foundMember) {
    boolean isBreadTypeSelected = memberBreadTypeRepository.existsByMember(foundMember);
    boolean isNutrientTypeSelected = memberNutrientTypeRepository.existsByMember(foundMember);
    boolean isMainPurposeSelected = !foundMember.getMainPurpose().equals(MainPurpose.NONE);

    return isBreadTypeSelected && isNutrientTypeSelected && isMainPurposeSelected;
  }

  private void setAlreadyFoundBakeryIds(
      List<Long> alreadyFoundBakeryIds, List<Bakery> bestBakeries) {
    alreadyFoundBakeryIds.addAll(
        bestBakeries.stream().map(Bakery::getBakeryId).collect(Collectors.toList()));
  }

  private void getRestRandomBakeries(List<Long> alreadyFoundBakeryIds, List<Bakery> bestBakeries) {
    log.info("########## 나머지만 랜덤으로 고르는 베이커리. 현재까지 조회된 베이커리 수: {} ##########", bestBakeries.size());
    setAlreadyFoundBakeryIds(alreadyFoundBakeryIds, bestBakeries);
    PageRequest bestPageRequest = PageRequest.of(0, MAX_BEST_BAKERY_COUNT - bestBakeries.size());
    bestBakeries.addAll(
        bakeryRepository.findRestBakeriesRandomly(alreadyFoundBakeryIds, bestPageRequest));
  }

  private void getRestBakeries(
      List<Long> alreadyFoundBakeryIds, List<BreadType> breadTypes, List<Bakery> bestBakeries) {
    log.info("########## 빵유형 일치 베이커리 조회 시작. 현재까지 조회된 베이커리 수: {} ##########", bestBakeries.size());
    setAlreadyFoundBakeryIds(alreadyFoundBakeryIds, bestBakeries);
    PageRequest bestPageRequest = PageRequest.of(0, MAX_BEST_BAKERY_COUNT - bestBakeries.size());
    bestBakeries.addAll(
        bakeryRepository.findRestBakeriesByBreadTypes(
            breadTypes, alreadyFoundBakeryIds, bestPageRequest));
  }

  private List<Bakery> getBestBakeries(Member foundMember, List<BreadType> breadTypes) {
    log.info("########## 베스트 건빵집 조회 시작 ##########");
    PageRequest bestPageRequest = PageRequest.of(0, MAX_BEST_BAKERY_COUNT);
    List<Bakery> bestBakeries =
        bakeryRepository.findBestBakeries(
            breadTypes, foundMember.getMainPurpose(), bestPageRequest);
    return bestBakeries;
  }

  public BakerySearchResponseDTO getBakeriesBySearch(String searchTerm) {
    if (searchTerm.isEmpty()) {
      return BakerySearchResponseDTO.getEmptyBakerySearchResponseDTO();
    }
    List<String> searchWordList = new ArrayList<>(Arrays.asList(searchTerm.split("\\s+")));

    Specification<Bakery> spec = Specification.where(null);

    for (String keyword : searchWordList) {
      Specification<Bakery> keywordSpec = BakerySpecifications.searchBakery(keyword);
      spec = spec.and(keywordSpec);
    }

    List<Bakery> foundBakeries = bakeryRepository.findAll(spec);
    List<BakeryListResponseDTO> bakeryListResponseDTOs =
        getBakeryListResponseDTOList(foundBakeries);

    return BakeryMapper.INSTANCE.toBakerySearchResponseDTO(
        bakeryListResponseDTOs.size(), bakeryListResponseDTOs);
  }

  private List<BakeryListResponseDTO> getBakeryListResponseDTOList(List<Bakery> foundBakeries) {
    List<BakeryListResponseDTO> bakeryListResponseDTOs = new ArrayList<>();

    for (Bakery foundBakery : foundBakeries) {
      List<BreadTypeResponseDTO> breadType =
          BakeryBreadTypeMapper.INSTANCE.toBreadTypeResponseDTOList(
              getBakeryBreadTypeList(foundBakery));
      BakeryListResponseDTO bakeryListResponseDTO =
          BakeryMapper.INSTANCE.toBakeryListResponseDTO(foundBakery, breadType);
      bakeryListResponseDTOs.add(bakeryListResponseDTO);
    }

    return bakeryListResponseDTOs;
  }

  private Page<BakeryListResponseDTO> getBakeryListResponseDTOList(Page<Bakery> foundBakeries) {
    List<BakeryListResponseDTO> bakeryListResponseDTOs = new ArrayList<>();

    for (Bakery foundBakery : foundBakeries) {
      List<BreadTypeResponseDTO> breadType =
          BakeryBreadTypeMapper.INSTANCE.toBreadTypeResponseDTOList(
              getBakeryBreadTypeList(foundBakery));
      BakeryListResponseDTO bakeryListResponseDTO =
          BakeryMapper.INSTANCE.toBakeryListResponseDTO(foundBakery, breadType);
      bakeryListResponseDTOs.add(bakeryListResponseDTO);
    }

    return new PageImpl<>(
        bakeryListResponseDTOs, foundBakeries.getPageable(), foundBakeries.getTotalElements());
  }

  private List<BakeryBreadType> getBakeryBreadTypeList(Bakery bakery) {
    return bakeryBreadTypeRepository.findAllByBakery(bakery);
  }

  public List<BakeryListResponseDTO> getBookMarkedBakeries(Long memberId) {
    List<Bakery> bookMarkedBakeries = bakeryRepository.findBookMarkedBakeries(memberId);
    return getBakeryListResponseDTOList(bookMarkedBakeries);
  }

  private boolean isBookMarked(Long memberId, Long bakeryId) {
    if (memberId == null) return false;
    return bookMarkRepository.findByMemberIdAndBakeryId(memberId, bakeryId).isPresent();
  }
}
