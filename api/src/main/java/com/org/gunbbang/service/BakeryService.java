package com.org.gunbbang.service;

import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.CategoryType;
import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.entity.*;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.*;
import com.org.gunbbang.util.Security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BakeryService {
    private final CategoryRepository categoryRepository;
    private final BakeryCategoryRepository bakeryCategoryRepository;
    private final BookMarkRepository bookMarkRepository;
    private final MemberRepository memberRepository;
    private final BakeryRepository bakeryRepository;
    private final MenuRepository menuRepository;




    private final int maxBestBakeryCount = 10;


    public List<BakeryListResponseDTO> getBakeryList(Long memberId, String sort, Boolean isHard, Boolean isDessert, Boolean isBrunch) {
        List<Category> categoryList = getCategoryList(isHard, isDessert, isBrunch);
        List<BakeryListResponseDTO> responseDtoList = new ArrayList<>();
        BreadTypeResponseDTO breadType;
        Boolean isBookMarked;
        List<BakeryCategory> bakeryCategoryList;
        Sort sortOption;

        if (categoryList.isEmpty()) {
            sortOption = sort.equals("review") ? Sort.by(Sort.Direction.DESC, "reviewCount") : Sort.by(Sort.Direction.DESC, "bakeryId");
            List<Bakery> bakeryList = bakeryRepository.findAll(sortOption);
            for (Bakery bakery : bakeryList) {
                breadType = getBreadType(bakery);
                isBookMarked = isBookMarked(memberId, bakery.getBakeryId());
                BakeryListResponseDTO bakeryListResponseDto = getBakeryResponseDTO(bakery, isBookMarked, breadType);
                responseDtoList.add(bakeryListResponseDto);
            }
            return responseDtoList;
        }

        sortOption = sort.equals("review") ? Sort.by(Sort.Direction.DESC, "bakery.reviewCount") : Sort.by(Sort.Direction.DESC, "bakery.bakeryId");
        bakeryCategoryList = bakeryCategoryRepository.findDistinctByCategoryIn(categoryList, sortOption);
        for (BakeryCategory bakeryCategory : bakeryCategoryList) {
            breadType = getBreadType(bakeryCategory.getBakery());
            isBookMarked = isBookMarked(memberId, bakeryCategory.getBakery().getBakeryId());
            BakeryListResponseDTO bakeryListResponseDto = getBakeryResponseDTO(bakeryCategory.getBakery(), isBookMarked, breadType);
            responseDtoList.add(bakeryListResponseDto);
        }
        return responseDtoList;
    }

    public BakeryDetailResponseDTO getBakeryDetail(Long memberId, Long bakeryId){
        Bakery bakery= bakeryRepository.findById(bakeryId).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));
        List<Menu> bakeryMenu = menuRepository.findAllByBakery(bakery);
        BreadTypeResponseDTO breadType = getBreadType(bakery);
        Boolean isBookMarked = isBookMarked(memberId, bakeryId);
        List<MenuResponseDTO> menuList = new ArrayList<>();

        for(Menu menu : bakeryMenu){
            menuList.add(MenuResponseDTO.builder()
                            .menuId(menu.getMenuId())
                            .menuName(menu.getMenuName())
                            .menuPrice(menu.getMenuPrice())
                            .build());
        }

        return BakeryDetailResponseDTO.builder()
                .bakeryId(bakery.getBakeryId())
                .bakeryName(bakery.getBakeryName())
                .bakeryPicture(bakery.getBakeryPicture())
                .isHACCP(bakery.getIsHACCP())
                .isVegan(bakery.getIsVegan())
                .isNonGMO(bakery.getIsNonGMO())
                .breadType(breadType)
                .firstNearStation(bakery.getFirstNearStation())
                .secondNearStation(bakery.getSecondNearStation())
                .isBookMarked(isBookMarked)
                .bookMarkCount(bakery.getBookMarkCount())
                .reviewCount(bakery.getReviewCount())
                .homepage(bakery.getHomepage())
                .address(bakery.getState()+" "+bakery.getCity()+" "+bakery.getTown()+" "+bakery.getAddressRest())
                .openingTime(bakery.getOpeningHours())
                .closedDay(bakery.getClosedDay())
                .phoneNumber(bakery.getPhoneNumber())
                .menuList(menuList)
                .build();
    }

    public List<BestBakeryListResponseDTO> getBestBakeries() {
        Long memberId = SecurityUtil.getLoginMemberId();
        List<Long> alreadyFoundBakeryIds = new ArrayList<>();
        alreadyFoundBakeryIds.add(Long.MAX_VALUE);

        Member foundMember = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));

        PageRequest bestPageRequest = PageRequest.of(0, maxBestBakeryCount);
        List<Bakery> bestBakeries = bakeryRepository.findBestBakeries(
                foundMember.getBreadType().getBreadTypeId(),
                foundMember.getMainPurpose(),
                bestPageRequest // TODO: 일케하는게맞냐????
        );

        if (bestBakeries.size() == maxBestBakeryCount) {
            log.info("베스트 베이커리 10개 조회 완료. 추가 조회 쿼리 없이 바로 반환");
            return getResponseBakeries(foundMember, bestBakeries);
        }

        System.out.println("alreadyFoundBakeryIds 엠티 여부: " + alreadyFoundBakeryIds);

        alreadyFoundBakeryIds = bestBakeries.stream().map(Bakery::getBakeryId).collect(Collectors.toList());

        log.info("빵유형 일치 베이커리 조회 시작");
        bestPageRequest = PageRequest.of(0, maxBestBakeryCount - bestBakeries.size());
        bestBakeries.addAll(bakeryRepository.findRestBakeriesByBreadTypeId(
                        foundMember.getBreadType(),
                        alreadyFoundBakeryIds,
                        bestPageRequest));

        if (bestBakeries.size() == maxBestBakeryCount) {
            log.info("빵유형 10개 조회 완료. 추가 조회 쿼리 없이 바로 반환");
            return getResponseBakeries(foundMember, bestBakeries);
        }

        alreadyFoundBakeryIds.addAll(
                bestBakeries.stream().map(Bakery::getBakeryId).collect(Collectors.toList())
        );

        bestPageRequest = PageRequest.of(0, maxBestBakeryCount - bestBakeries.size());
        log.info("찐 나머지만 고르는 베이커리");
        bestBakeries.addAll(bakeryRepository.findRestBakeriesRandomly(
                alreadyFoundBakeryIds,
                bestPageRequest));

        return getResponseBakeries(foundMember, bestBakeries);

    }

    // TODO: 이거 DTO 안에 static 메서드로 못빼나??
    private List<BestBakeryListResponseDTO> getResponseBakeries(Member member, List<Bakery> bakeries) {
        List<BestBakeryListResponseDTO> responseDtoList = new ArrayList<>();
        for (Bakery bestBakery: bakeries) {
            Boolean isBookMarked = isBookMarked(member.getMemberId(), bestBakery.getBakeryId());
            BestBakeryListResponseDTO response = BestBakeryListResponseDTO.builder()
                    .bakeryId(bestBakery.getBakeryId())
                    .bakeryName(bestBakery.getBakeryName())
                    .bakeryPicture(bestBakery.getBakeryPicture())
                    .isHACCP(bestBakery.getIsHACCP())
                    .isVegan(bestBakery.getIsVegan())
                    .isNonGMO(bestBakery.getIsNonGMO())
                    .firstNearStation(bestBakery.getFirstNearStation())
                    .secondNearStation(bestBakery.getSecondNearStation())
                    .isBookMarked(isBookMarked)
                    .bookMarkCount(bestBakery.getBookMarkCount())
                    .reviewCount(bestBakery.getReviewCount())
                    .build();
            responseDtoList.add(response);
        }
        return responseDtoList;
    }

    public BakerySearchResponseDTO getBakeriesByName(String bakeryName, Long memberId) {
        if (bakeryName.isEmpty()) {
            return BakerySearchResponseDTO.getEmptyBakerySearchResponseDTO();
        }

        List<Bakery> foundBakeries = bakeryRepository.findBakeryByBakeryName(bakeryName);
        List<BakeryListResponseDTO> bakeryListResponseDTOs = new ArrayList<>();
        for (Bakery foundBakery : foundBakeries) {
            Boolean isBookMarked = isBookMarked(memberId, foundBakery.getBakeryId());
            BreadTypeResponseDTO breadType = getBreadType(foundBakery);
            BakeryListResponseDTO bakeryListResponseDto = getBakeryResponseDTO(foundBakery, isBookMarked, breadType);
            bakeryListResponseDTOs.add(bakeryListResponseDto);
        }

        return BakerySearchResponseDTO.builder()
                .resultCount(foundBakeries.size())
                .bakeryList(bakeryListResponseDTOs)
                .build();
    }

    public List<BakeryListResponseDTO> getBookMarkedBakeries(Long memberId) {
        List<Bakery> bookMarkedBakeries = bakeryRepository.findBookMarkedBakeries(memberId);
        List<BakeryListResponseDTO> bakeryListResponseDTOs = new ArrayList<>();
        for (Bakery bookMarkedBakery : bookMarkedBakeries) {
            BreadTypeResponseDTO breadType = getBreadType(bookMarkedBakery);
            BakeryListResponseDTO bakeryListResponseDto = getBakeryResponseDTO(bookMarkedBakery, true, breadType);
            bakeryListResponseDTOs.add(bakeryListResponseDto);
        }
        return bakeryListResponseDTOs;
    }

    private Boolean isBookMarked(Long memberId, Long bakeryId) {
        if (bookMarkRepository.findByMemberIdAndBakeryId(memberId, bakeryId).isPresent()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private BreadTypeResponseDTO getBreadType(Bakery bakery){
        return BreadTypeResponseDTO.builder()
                .breadTypeId(bakery.getBreadType().getBreadTypeId())
                .breadTypeName(bakery.getBreadType().getBreadTypeName())
                .isGlutenFree(bakery.getBreadType().getIsGlutenFree())
                .isVegan(bakery.getBreadType().getIsVegan())
                .isNutFree(bakery.getBreadType().getIsNutFree())
                .isSugarFree(bakery.getBreadType().getIsSugarFree())
                .build();
    }

    private BakeryListResponseDTO getBakeryResponseDTO(Bakery bakery, Boolean isBookMarked, BreadTypeResponseDTO breadType ){
        return BakeryListResponseDTO.builder()
                .bakeryId(bakery.getBakeryId())
                .bakeryName(bakery.getBakeryName())
                .bakeryPicture(bakery.getBakeryPicture())
                .isHACCP(bakery.getIsHACCP())
                .isVegan(bakery.getIsVegan())
                .isNonGMO(bakery.getIsNonGMO())
                .firstNearStation(bakery.getFirstNearStation())
                .secondNearStation(bakery.getSecondNearStation())
                .isBookMarked(isBookMarked)
                .bookMarkCount(bakery.getBookMarkCount())
                .reviewCount(bakery.getReviewCount())
                .breadType(breadType)
                .build();
    }

    private List<Category> getCategoryList(Boolean isHard, Boolean isDessert, Boolean isBrunch){
        List<Category> categoryList = new ArrayList<>();
        if (isHard) {
            categoryList.add(categoryRepository.findByCategoryName(CategoryType.HARD_BREAD.getName()).orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_CATEGORY_EXCEPTION)));
        }
        if (isDessert) {
            categoryList.add(categoryRepository.findByCategoryName(CategoryType.DESSERT.getName()).orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_CATEGORY_EXCEPTION)));
        }
        if (isBrunch) {
            categoryList.add(categoryRepository.findByCategoryName(CategoryType.BRUNCH.getName()).orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_CATEGORY_EXCEPTION)));
        }
        return categoryList;
    }
}
