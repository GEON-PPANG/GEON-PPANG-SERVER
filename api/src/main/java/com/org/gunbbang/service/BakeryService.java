package com.org.gunbbang.service;

import com.org.gunbbang.CategoryType;
import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseBakeryResponseDTOV2;
import com.org.gunbbang.entity.*;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.*;
import com.org.gunbbang.service.specification.BakerySpecifications;
import com.org.gunbbang.util.mapper.BakeryMapper;
import com.org.gunbbang.util.mapper.BreadTypeMapper;
import com.org.gunbbang.util.mapper.MenuMapper;
import com.org.gunbbang.util.security.SecurityUtil;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BakeryService {
  private final CategoryRepository categoryRepository;
  private final BookMarkRepository bookMarkRepository;
  private final MemberRepository memberRepository;
  private final BakeryRepository bakeryRepository;
  private final MenuRepository menuRepository;
  private final BreadTypeRepository breadTypeRepository;
  private final BakeryCategoryRepository bakeryCategoryRepository;

  private final String BLANK_SPACE = " ";
  private final int maxBestBakeryCount = 10;

  public List<BakeryListResponseDTO> getBakeryList(
      String sortingOption,
      boolean personalFilter,
      boolean isHard,
      boolean isDessert,
      boolean isBrunch) {
    Long memberBreadTypeId = SecurityUtil.getLoginMemberBreadTypeId();
    List<Category> categoryList = getCategoryList(isHard, isDessert, isBrunch);
    List<Bakery> bakeryList =
        getFilteredAndSortedBakeryList(
            personalFilter, memberBreadTypeId, categoryList, sortingOption);
    return getBakeryListResponseDTOList(bakeryList);
  }

  private List<Category> getCategoryList(boolean isHard, boolean isDessert, boolean isBrunch) {
    List<Category> categoryList = new ArrayList<>();

    Map<CategoryType, Boolean> categoryMap = new HashMap<>();
    categoryMap.put(CategoryType.HARD_BREAD, isHard);
    categoryMap.put(CategoryType.DESSERT, isDessert);
    categoryMap.put(CategoryType.BRUNCH, isBrunch);

    for (Map.Entry<CategoryType, Boolean> entry : categoryMap.entrySet()) {
      if (entry.getValue()) { // true인 값만 해당되어 category에 추가된다
        Category category =
            categoryRepository
                .findByCategoryName(entry.getKey().getName())
                .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_CATEGORY_EXCEPTION));
        categoryList.add(category);
      }
    }

    if (categoryList.isEmpty()) { // 카테고리가 빈 경우
      for (CategoryType categoryType : CategoryType.values()) {
        Category category =
            categoryRepository
                .findByCategoryName(categoryType.getName())
                .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_CATEGORY_EXCEPTION));
        categoryList.add(category);
      }
    }

    return categoryList;
  }

  private List<Bakery> getFilteredAndSortedBakeryList(
      boolean personalFilter, Long breadTypeId, List<Category> categoryList, String sortingOption) {
    BreadType breadType =
        personalFilter
            ? breadTypeRepository
                .findById(breadTypeId)
                .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BREAD_TYPE_EXCEPTION))
            : breadTypeRepository
                .findBreadTypeByIsGlutenFreeAndIsVeganAndIsNutFreeAndIsSugarFree(
                    true, true, true, true)
                .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BREAD_TYPE_EXCEPTION));

    List<Bakery> filteredBakeryList =
        bakeryRepository.findFilteredBakeries(
            categoryList,
            breadType.getIsGlutenFree(),
            breadType.getIsVegan(),
            breadType.getIsNutFree(),
            breadType.getIsSugarFree());

    List<Bakery> getSortedByCategoryBakeryList = getSortedByCategoryBakeryList(filteredBakeryList);

    if ("review".equals(sortingOption)) {
      getSortedByCategoryBakeryList.sort(
          Comparator.comparingLong(Bakery::getReviewCount).reversed());
      return getSortedByCategoryBakeryList;
    }

    getSortedByCategoryBakeryList.sort(Comparator.comparingLong(Bakery::getBakeryId).reversed());
    return getSortedByCategoryBakeryList;
  }

  private List<Bakery> getSortedByCategoryBakeryList(List<Bakery> bakeryList) {
    Map<Bakery, Long> bakeryCategoryCounts = new HashMap<>();

    for (Bakery bakery : bakeryList) {
      long count = bakeryCategoryRepository.countBakeryCategoriesByBakery(bakery);
      System.out.println("bakeryName:" + bakery.getBakeryName() + "count:" + count);
      bakeryCategoryCounts.put(bakery, count);
    }

    return bakeryCategoryCounts.entrySet().stream()
        .sorted(Map.Entry.<Bakery, Long>comparingByValue().reversed())
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
  }

  public BakeryDetailResponseDTO getBakeryDetail(Long memberId, Long bakeryId) {
    Bakery bakery =
        bakeryRepository
            .findById(bakeryId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));
    BreadTypeResponseDTO breadType =
        BreadTypeMapper.INSTANCE.toBreadTypeResponseDTO(bakery.getBreadType());
    boolean isBookMarked = isBookMarked(memberId, bakeryId);
    List<Menu> bakeryMenuList = menuRepository.findAllByBakery(bakery);
    List<MenuResponseDTO> menuList = MenuMapper.INSTANCE.toMenuResponseDTOList(bakeryMenuList);
    String address =
        getAddress(bakery.getState(), bakery.getCity(), bakery.getTown(), bakery.getAddressRest());

    return BakeryMapper.INSTANCE.toBakeryDetailResponseDTO(
        bakery, address, breadType, isBookMarked, menuList);
  }

  String getAddress(String state, String city, String town, String addressRest) {
    return state + BLANK_SPACE + city + BLANK_SPACE + town + BLANK_SPACE + addressRest;
  }

  public List<BestBakeryListResponseDTO> getBestBakeries(Long memberId) {
    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));

    if (isFilterNotSelected(foundMember)) {
      log.info("회원이 필터 선택 안한 경우. 랜덤으로 10개 건빵집 반환");
      List<Bakery> randomBakeries = getOnlyRandomBakeries();
      return BakeryMapper.INSTANCE.toBestBakeryListResponseDTO(randomBakeries);
    }

    List<Bakery> bestBakeries = getBestBakeries(foundMember);
    if (bestBakeries.size() == maxBestBakeryCount) {
      log.info("베스트 베이커리 10개 조회 완료. 추가 조회 쿼리 없이 바로 반환");
      return BakeryMapper.INSTANCE.toBestBakeryListResponseDTO(bestBakeries);
    }

    List<Long> alreadyFoundBakeryIds = new ArrayList<>();
    alreadyFoundBakeryIds.add(-1L);
    getRestBakeries(alreadyFoundBakeryIds, foundMember.getBreadType(), bestBakeries);

    if (bestBakeries.size() == maxBestBakeryCount) {
      log.info("빵유형 10개 조회 완료. 추가 조회 쿼리 없이 바로 반환");
      return BakeryMapper.INSTANCE.toBestBakeryListResponseDTO(bestBakeries);
    }

    getRestRandomBakeries(alreadyFoundBakeryIds, bestBakeries);
    return BakeryMapper.INSTANCE.toBestBakeryListResponseDTO(bestBakeries);
  }

  private List<Bakery> getOnlyRandomBakeries() {
    PageRequest bestPageRequest = PageRequest.of(0, maxBestBakeryCount);
    return bakeryRepository.findBakeriesRandomly(bestPageRequest);
  }

  private static boolean isFilterNotSelected(Member foundMember) {
    return foundMember.getBreadType().getIsGlutenFree() == false
        && foundMember.getBreadType().getIsNutFree() == false
        && foundMember.getBreadType().getIsSugarFree() == false
        && foundMember.getBreadType().getIsVegan() == false
        && foundMember.getMainPurpose() == MainPurpose.NONE;
  }

  private void setAlreadyFoundBakeryIds(
      List<Long> alreadyFoundBakeryIds, List<Bakery> bestBakeries) {
    alreadyFoundBakeryIds.addAll(
        bestBakeries.stream().map(Bakery::getBakeryId).collect(Collectors.toList()));
  }

  private void getRestRandomBakeries(List<Long> alreadyFoundBakeryIds, List<Bakery> bestBakeries) {
    log.info("나머지만 랜덤으로 고르는 베이커리. 현재까지 조회된 베이커리 수: " + bestBakeries.size());
    setAlreadyFoundBakeryIds(alreadyFoundBakeryIds, bestBakeries);
    PageRequest bestPageRequest = PageRequest.of(0, maxBestBakeryCount - bestBakeries.size());
    bestBakeries.addAll(
        bakeryRepository.findRestBakeriesRandomly(alreadyFoundBakeryIds, bestPageRequest));
  }

  private void getRestBakeries(
      List<Long> alreadyFoundBakeryIds, BreadType breadType, List<Bakery> bestBakeries) {
    log.info("빵유형 일치 베이커리 조회 시작. 현재까지 조회된 베이커리 수: " + bestBakeries.size());
    setAlreadyFoundBakeryIds(alreadyFoundBakeryIds, bestBakeries);
    PageRequest bestPageRequest = PageRequest.of(0, maxBestBakeryCount - bestBakeries.size());
    bestBakeries.addAll(
        bakeryRepository.findRestBakeriesByBreadTypeId(
            breadType, alreadyFoundBakeryIds, bestPageRequest));
  }

  private List<Bakery> getBestBakeries(Member foundMember) {
    log.info("베스트 건빵집 조회 시작.");
    PageRequest bestPageRequest = PageRequest.of(0, maxBestBakeryCount);
    List<Bakery> bestBakeries =
        bakeryRepository.findBestBakeries(
            foundMember.getBreadType().getBreadTypeId(),
            foundMember.getMainPurpose(),
            bestPageRequest);
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
      BreadTypeResponseDTO breadType =
          BreadTypeMapper.INSTANCE.toBreadTypeResponseDTO(foundBakery.getBreadType());
      BakeryListResponseDTO bakeryListResponseDTO =
          BakeryMapper.INSTANCE.toBakeryListResponseDTO(foundBakery, breadType);
      bakeryListResponseDTOs.add(bakeryListResponseDTO);
    }

    return bakeryListResponseDTOs;
  }

  public BakerySearchResponseDTOV2 getBakeriesByNameV2(String bakeryName, Long memberId) {
    if (bakeryName.isEmpty()) {
      return BakerySearchResponseDTOV2.getEmptyBakerySearchResponseDTO();
    }

    List<Bakery> foundBakeries = bakeryRepository.findBakeryByBakeryName(bakeryName);
    List<BakeryListResponseDTOV2> bakeryListResponseDTOs = new ArrayList<>();
    for (Bakery foundBakery : foundBakeries) {
      boolean isBookMarked = isBookMarked(memberId, foundBakery.getBakeryId());
      BreadTypeResponseDTO breadType =
          BreadTypeMapper.INSTANCE.toBreadTypeResponseDTO(foundBakery.getBreadType());
      BakeryListResponseDTOV2 bakeryListResponseDTOV2 =
          getBakeryResponseDTOV2(foundBakery, isBookMarked, breadType);
      bakeryListResponseDTOs.add(bakeryListResponseDTOV2);
    }

    return BakerySearchResponseDTOV2.builder()
        .resultCount(foundBakeries.size())
        .bakeryList(bakeryListResponseDTOs)
        .build();
  }

  public List<BakeryListResponseDTO> getBookMarkedBakeries(Long memberId) {
    List<Bakery> bookMarkedBakeries = bakeryRepository.findBookMarkedBakeries(memberId);
    return getBakeryListResponseDTOList(bookMarkedBakeries);
  }

  // TODO: 공통적으로 사용되는 메서드라 다른 곳으로는 못뺄지
  private boolean isBookMarked(Long memberId, Long bakeryId) {
    if (bookMarkRepository.findByMemberIdAndBakeryId(memberId, bakeryId).isPresent()) {
      return true;
    }
    return false;
  }

  private BakeryListResponseDTOV2 getBakeryResponseDTOV2(
      Bakery bakery, boolean isBookMarked, BreadTypeResponseDTO breadType) {
    BaseBakeryResponseDTOV2 baseBakeryResponseDTOV2 =
        BaseBakeryResponseDTOV2.builder()
            .bakeryId(bakery.getBakeryId())
            .bakeryName(bakery.getBakeryName())
            .bakeryPicture(bakery.getBakeryPicture())
            .isHACCP(bakery.getIsHACCP())
            .isVegan(bakery.getIsVegan())
            .isNonGMO(bakery.getIsNonGMO())
            .firstNearStation(bakery.getFirstNearStation())
            .secondNearStation(bakery.getSecondNearStation())
            .build();

    return BakeryListResponseDTOV2.builder()
        .baseBakeryResponseDTOV2(baseBakeryResponseDTOV2)
        .breadType(breadType)
        .reviewCount(bakery.getReviewCount())
        .isBookMarked(isBookMarked)
        .build();
  }
}
