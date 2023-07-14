package com.org.gunbbang.service;

import com.org.gunbbang.BadRequestException;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.entity.*;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.*;
import com.org.gunbbang.util.Security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        List<Category> categoryIdList = new ArrayList<>();
        List<BakeryCategory> bakeryCategoryList;
        List<BakeryListResponseDTO> responseDtoList = new ArrayList();
        BakeryListResponseDTO bakeryListResponseDto;
        BreadTypeResponseDTO breadTypeResponseDto;
        Boolean isBooked;


        if (Boolean.TRUE.equals(isHard)) {
            categoryIdList.add(categoryRepository.findByCategoryName("하드빵류").orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_CATEGORY_EXCEPTION)));
        }
        if (Boolean.TRUE.equals(isDessert)) {
            categoryIdList.add(categoryRepository.findByCategoryName("디저트류").orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_CATEGORY_EXCEPTION)));
        }
        if (Boolean.TRUE.equals(isBrunch)) {
            categoryIdList.add(categoryRepository.findByCategoryName("브런치류").orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_CATEGORY_EXCEPTION)));
        }


        if (sort.equals("reivew")) {
            bakeryCategoryList = bakeryCategoryRepository.findByBakeryCategoryIdAndReview(categoryIdList);
        } else {
            bakeryCategoryList = bakeryCategoryRepository.findByBakeryCategoryId(categoryIdList);
        }

        for (BakeryCategory bakeryCategory : bakeryCategoryList) {
            breadTypeResponseDto = BreadTypeResponseDTO.builder()
                    .breadTypeId(bakeryCategory.getBakery().getBreadType().getBreadTypeId())
                    .breadTypeName(bakeryCategory.getBakery().getBreadType().getBreadTypeName())
                    .isGlutenFree(bakeryCategory.getBakery().getBreadType().getIsGlutenFree())
                    .isVegan(bakeryCategory.getBakery().getBreadType().getIsVegan())
                    .isNutFree(bakeryCategory.getBakery().getBreadType().getIsNutFree())
                    .isSugarFree(bakeryCategory.getBakery().getBreadType().getIsSugarFree())
                    .build();

            if (bookMarkRepository.findByMemberAndBakery(memberRepository.findById(memberId).orElseThrow(()->new BadRequestException(ErrorType.REQUEST_VALIDATION_EXCEPTION)), bakeryCategory.getBakery()).isPresent()) {
                isBooked = Boolean.TRUE;
            } else {
                isBooked = Boolean.FALSE;
            }

            bakeryListResponseDto = BakeryListResponseDTO.builder()
                    .bakeryId(bakeryCategory.getBakery().getBakeryId())
                    .bakeryName(bakeryCategory.getBakery().getBakeryName())
                    .bakeryPicture(bakeryCategory.getBakery().getBakeryPicture())
                    .isHACCP(bakeryCategory.getBakery().getIsHACCP())
                    .isVegan(bakeryCategory.getBakery().getIsVegan())
                    .isNonGMO(bakeryCategory.getBakery().getIsNonGMO())
                    .breadType(breadTypeResponseDto)
                    .firstNearStation(bakeryCategory.getBakery().getFirstNearStation())
                    .secondNearStation(bakeryCategory.getBakery().getSecondNearStation())
                    .isBooked(isBooked)
                    .bookMarkCount(bakeryCategory.getBakery().getBookMarkCount())
                    .build();

            responseDtoList.add(bakeryListResponseDto);
        }
        return responseDtoList;
    }

    public BakeryDetailResponseDTO getBakeryDetail(Long memberId, Long bakeryId){
        Boolean isBooked;

        Bakery bakery= bakeryRepository.findById(bakeryId).orElseThrow(()->new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));

        List<Menu> bakeryMenu = menuRepository.findAllByBakery(bakery);

        BreadTypeResponseDTO breadTypeResponseDto = BreadTypeResponseDTO.builder()
                .breadTypeId(bakery.getBreadType().getBreadTypeId())
                .breadTypeName(bakery.getBreadType().getBreadTypeName())
                .isGlutenFree(bakery.getBreadType().getIsGlutenFree())
                .isVegan(bakery.getBreadType().getIsVegan())
                .isNutFree(bakery.getBreadType().getIsNutFree())
                .isSugarFree(bakery.getBreadType().getIsSugarFree())
                .build();

        if (bookMarkRepository.findByMemberAndBakery(memberRepository.findById(memberId).orElseThrow(()->new BadRequestException(ErrorType.REQUEST_VALIDATION_EXCEPTION)), bakery).isPresent()) {
            isBooked = Boolean.TRUE;
        } else {
            isBooked = Boolean.FALSE;
        }

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
                .breadType(breadTypeResponseDto)
                .firstNearStation(bakery.getFirstNearStation())
                .secondNearStation(bakery.getSecondNearStation())
                .isBooked(isBooked)
                .bookMarkCount(bakery.getBookMarkCount())
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
            return getResponseBakeries(foundMember, bestBakeries);
        }

        List<Long> alreadyFoundBakeries = bestBakeries.stream().map(Bakery::getBakeryId).collect(Collectors.toList());
        PageRequest restPageRequest = PageRequest.of(0, maxBestBakeryCount - bestBakeries.size());
        bestBakeries.addAll(bakeryRepository.findRestBakeriesByBreadTypeId(
                        foundMember.getBreadType(),
                        alreadyFoundBakeries,
                        restPageRequest));

        return getResponseBakeries(foundMember, bestBakeries);

    }

    // TODO: 이거 DTO 안에 static 메서드로 못빼나??
    private List<BestBakeryListResponseDTO> getResponseBakeries(Member member, List<Bakery> bakeries) {
        List<BestBakeryListResponseDTO> responseDtoList = new ArrayList();
        for (Bakery bestBakery: bakeries) {
            Boolean isBooked = isBooked(member.getMemberId(), bestBakery.getBakeryId());

            BestBakeryListResponseDTO response = BestBakeryListResponseDTO.builder()
                    .bakeryId(bestBakery.getBakeryId())
                    .bakeryName(bestBakery.getBakeryName())
                    .bakeryPicture(bestBakery.getBakeryPicture())
                    .isHACCP(bestBakery.getIsHACCP())
                    .isVegan(bestBakery.getIsVegan())
                    .isNonGMO(bestBakery.getIsNonGMO())
                    .firstNearStation(bestBakery.getFirstNearStation())
                    .secondNearStation(bestBakery.getSecondNearStation())
                    .isBooked(isBooked)
                    .bookMarkCount(bestBakery.getBookMarkCount())
                    .reviewCount(bestBakery.getReviewCount())
                    .build();

            responseDtoList.add(response);
        }
        return responseDtoList;
    }

    public BakerySearchResponseDTO getBakeriesByName(String bakeryName, Long memberId) {
        List<Bakery> foundBakeries = bakeryRepository.findBakeryByBakeryName(bakeryName);
        List<BakeryListResponseDTO> bakeryListResponseDTOs = new ArrayList<>();
        for (Bakery foundBakery : foundBakeries) {
            Boolean isBooked = isBooked(memberId, foundBakery.getBakeryId());

            BreadTypeResponseDTO breadTypeResponseDto = BreadTypeResponseDTO.builder()
                    .breadTypeId(foundBakery.getBreadType().getBreadTypeId())
                    .breadTypeName(foundBakery.getBreadType().getBreadTypeName())
                    .isGlutenFree(foundBakery.getBreadType().getIsGlutenFree())
                    .isVegan(foundBakery.getBreadType().getIsVegan())
                    .isNutFree(foundBakery.getBreadType().getIsNutFree())
                    .isSugarFree(foundBakery.getBreadType().getIsSugarFree())
                    .build();


            BakeryListResponseDTO bakeryListResponseDto = BakeryListResponseDTO.builder()
                    .bakeryId(foundBakery.getBakeryId())
                    .bakeryName(foundBakery.getBakeryName())
                    .bakeryPicture(foundBakery.getBakeryPicture())
                    .isHACCP(foundBakery.getIsHACCP())
                    .isVegan(foundBakery.getIsVegan())
                    .isNonGMO(foundBakery.getIsNonGMO())
                    .firstNearStation(foundBakery.getFirstNearStation())
                    .secondNearStation(foundBakery.getSecondNearStation())
                    .isBooked(isBooked)
                    .bookMarkCount(foundBakery.getBookMarkCount())
                    .breadType(breadTypeResponseDto)
                    .build();

            bakeryListResponseDTOs.add(bakeryListResponseDto);
        }


        BakerySearchResponseDTO bakerySearchResponseDTO = BakerySearchResponseDTO.builder()
                .resultCount(foundBakeries.size())
                .bakeryList(bakeryListResponseDTOs)
                .build();

        return bakerySearchResponseDTO;
    }

    private Boolean isBooked(Long memberId, Long bakeryId) {
        Boolean isBooked = Boolean.FALSE;
        if (bookMarkRepository.findByMemberIdAndBakeryId(memberId, bakeryId).isPresent()) {
            isBooked = Boolean.TRUE;
        }
        return isBooked;
    }

    public List<BakeryListResponseDTO> getBookMarkedBakeries(Long memberId) {
        List<Bakery> bookMarkedBakeries = bakeryRepository.findBookMarkedBakeries(memberId);
        List<BakeryListResponseDTO> bakeryListResponseDTOs = new ArrayList<>();
        for (Bakery bookMarkedBakery : bookMarkedBakeries) {
            BreadTypeResponseDTO breadTypeResponseDto = BreadTypeResponseDTO.builder()
                    .breadTypeId(bookMarkedBakery.getBreadType().getBreadTypeId())
                    .breadTypeName(bookMarkedBakery.getBreadType().getBreadTypeName())
                    .isGlutenFree(bookMarkedBakery.getBreadType().getIsGlutenFree())
                    .isVegan(bookMarkedBakery.getBreadType().getIsVegan())
                    .isNutFree(bookMarkedBakery.getBreadType().getIsNutFree())
                    .isSugarFree(bookMarkedBakery.getBreadType().getIsSugarFree())
                    .build();


            BakeryListResponseDTO bakeryListResponseDto = BakeryListResponseDTO.builder()
                    .bakeryId(bookMarkedBakery.getBakeryId())
                    .bakeryName(bookMarkedBakery.getBakeryName())
                    .bakeryPicture(bookMarkedBakery.getBakeryPicture())
                    .isHACCP(bookMarkedBakery.getIsHACCP())
                    .isVegan(bookMarkedBakery.getIsVegan())
                    .isNonGMO(bookMarkedBakery.getIsNonGMO())
                    .firstNearStation(bookMarkedBakery.getFirstNearStation())
                    .secondNearStation(bookMarkedBakery.getSecondNearStation())
                    .isBooked(true)
                    .bookMarkCount(bookMarkedBakery.getBookMarkCount())
                    .breadType(breadTypeResponseDto)
                    .build();

            bakeryListResponseDTOs.add(bakeryListResponseDto);
        }

        return bakeryListResponseDTOs;

    }
}
