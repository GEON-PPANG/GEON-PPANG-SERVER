package com.org.gunbbang.service;

import com.org.gunbbang.BadRequestException;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.controller.DTO.response.BakeryDetailResponseDto;
import com.org.gunbbang.controller.DTO.response.BakeryListResponseDto;
import com.org.gunbbang.controller.DTO.response.BreadTypeResponseDto;
import com.org.gunbbang.controller.DTO.response.MenuResponseDto;
import com.org.gunbbang.entity.Bakery;
import com.org.gunbbang.entity.BakeryCategory;
import com.org.gunbbang.entity.Category;
import com.org.gunbbang.entity.Menu;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BakeryService {
    private final CategoryRepository categoryRepository;
    private final BakeryCategoryRepository bakeryCategoryRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final BakeryRepository bakeryRepository;
    private final MenuRepository menuRepository;

    public List<BakeryListResponseDto> getBakeryList(Long memberId, String sort, Boolean isHard, Boolean isDessert, Boolean isBrunch) {
        List<Category> categoryIdList = new ArrayList<>();
        List<BakeryCategory> bakeryCategoryList;
        List<BakeryListResponseDto> responseDtoList = new ArrayList();
        BakeryListResponseDto bakeryListResponseDto;
        BreadTypeResponseDto breadTypeResponseDto;
        Boolean isBooked;


        if (Boolean.TRUE.equals(isHard)) {
            categoryIdList.add(categoryRepository.findByCategoryName("하드빵류").orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION)));
        }
        if (Boolean.TRUE.equals(isDessert)) {
            categoryIdList.add(categoryRepository.findByCategoryName("디저트류").orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION)));
        }
        if (Boolean.TRUE.equals(isBrunch)) {
            categoryIdList.add(categoryRepository.findByCategoryName("브런치류").orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION)));
        }


        if (sort.equals("reivew")) {
            bakeryCategoryList = bakeryCategoryRepository.findByBakeryCategoryIdAndReview(categoryIdList);
        } else {
            bakeryCategoryList = bakeryCategoryRepository.findByBakeryCategoryId(categoryIdList);
        }

        for (BakeryCategory bakeryCategory : bakeryCategoryList) {
            breadTypeResponseDto = BreadTypeResponseDto.builder()
                    .breadTypeId(bakeryCategory.getBakeryId().getBreadTypeId().getBreadTypeId())
                    .breadTypeName(bakeryCategory.getBakeryId().getBreadTypeId().getBreadTypeName())
                    .isGlutenFree(bakeryCategory.getBakeryId().getBreadTypeId().getIsGlutenFree())
                    .isVegan(bakeryCategory.getBakeryId().getBreadTypeId().getIsVegan())
                    .isNutFree(bakeryCategory.getBakeryId().getBreadTypeId().getIsNutFree())
                    .isSugarFree(bakeryCategory.getBakeryId().getBreadTypeId().getIsSugarFree())
                    .build();

            if (bookmarkRepository.findByMemberIdAndBakeryId(memberRepository.findById(memberId).orElseThrow(()->new BadRequestException(ErrorType.REQUEST_VALIDATION_EXCEPTION)), bakeryCategory.getBakeryId()).isPresent()) {
                isBooked = Boolean.TRUE;
            } else {
                isBooked = Boolean.FALSE;
            }

            bakeryListResponseDto = BakeryListResponseDto.builder()
                    .bakeryId(bakeryCategory.getBakeryId().getBakeryId())
                    .bakeryName(bakeryCategory.getBakeryId().getBakeryName())
                    .bakeryPicture(bakeryCategory.getBakeryId().getBakeryPicture())
                    .isHACCP(bakeryCategory.getBakeryId().getIsHACCP())
                    .isVegan(bakeryCategory.getBakeryId().getIsVegan())
                    .isNonGMO(bakeryCategory.getBakeryId().getIsNonGMO())
                    .breadTypeResponseDto(breadTypeResponseDto)
                    .firstNearStation(bakeryCategory.getBakeryId().getFirstNearStation())
                    .secondNearStation(bakeryCategory.getBakeryId().getSecondNearStation())
                    .isBooked(isBooked)
                    .bookmarkCount(bakeryCategory.getBakeryId().getBookmarkCount().intValue())
                    .build();

            responseDtoList.add(bakeryListResponseDto);
        }
        return responseDtoList;
    }

    public BakeryDetailResponseDto getBakeryDetail(Long memberId, Long bakeryId){
        Boolean isBooked;

        Bakery bakery= bakeryRepository.findById(bakeryId).orElseThrow(()->new NotFoundException(ErrorType.INVALID_BAKERY_EXCEPTION));

        List<Menu> bakeryMenu = menuRepository.findAllByBakeryId(bakery);

        BreadTypeResponseDto breadTypeResponseDto = BreadTypeResponseDto.builder()
                .breadTypeId(bakery.getBreadTypeId().getBreadTypeId())
                .breadTypeName(bakery.getBreadTypeId().getBreadTypeName())
                .isGlutenFree(bakery.getBreadTypeId().getIsGlutenFree())
                .isVegan(bakery.getBreadTypeId().getIsVegan())
                .isNutFree(bakery.getBreadTypeId().getIsNutFree())
                .isSugarFree(bakery.getBreadTypeId().getIsSugarFree())
                .build();

        if (bookmarkRepository.findByMemberIdAndBakeryId(memberRepository.findById(memberId).orElseThrow(()->new BadRequestException(ErrorType.REQUEST_VALIDATION_EXCEPTION)), bakery).isPresent()) {
            isBooked = Boolean.TRUE;
        } else {
            isBooked = Boolean.FALSE;
        }

        List<MenuResponseDto> menuList = new ArrayList<>();

        for(Menu menu : bakeryMenu){
            menuList.add(MenuResponseDto.builder()
                            .menuId(menu.getMenuId())
                            .menuName(menu.getMenuName())
                            .menuPrice(menu.getMenuPrice())
                            .build());
        }

        return BakeryDetailResponseDto.builder()
                .bakeryId(bakery.getBakeryId())
                .bakeryName(bakery.getBakeryName())
                .bakeryPicture(bakery.getBakeryPicture())
                .isHACCP(bakery.getIsHACCP())
                .isVegan(bakery.getIsVegan())
                .isNonGMO(bakery.getIsNonGMO())
                .breadTypeResponseDto(breadTypeResponseDto)
                .firstNearStation(bakery.getFirstNearStation())
                .secondNearStation(bakery.getSecondNearStation())
                .isBooked(isBooked)
                .bookmarkCount(bakery.getBookmarkCount().intValue())
                .homepage(bakery.getHomepage())
                .openingTime(bakery.getOpeningHours())
                .closedDay(bakery.getClosedDay())
                .phoneNumber(bakery.getPhoneNumber())
                .menuList(menuList)
                .build();
    }
}
