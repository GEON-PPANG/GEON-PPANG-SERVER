package com.org.gunbbang.service;

import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.controller.DTO.response.BakeryListResponseDto;
import com.org.gunbbang.controller.DTO.response.BreadTypeResponseDto;
import com.org.gunbbang.entity.BakeryCategory;
import com.org.gunbbang.entity.Category;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.BakeryCategoryRepository;
import com.org.gunbbang.repository.BookmarkRepository;
import com.org.gunbbang.repository.CategoryRepository;
import com.org.gunbbang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BakeryService {
    private final CategoryRepository categoryRepository;
    private final BakeryCategoryRepository bakeryCategoryRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;

    @Transactional
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
                    .isGlutenFree(bakeryCategory.getBakeryId().getBreadTypeId().isGlutenFree())
                    .isVegan(bakeryCategory.getBakeryId().getBreadTypeId().isVegan())
                    .isNutFree(bakeryCategory.getBakeryId().getBreadTypeId().isNutFree())
                    .isSugarFree(bakeryCategory.getBakeryId().getBreadTypeId().isSugarFree())
                    .build();

            if (bookmarkRepository.findByMemberIdAndBakeryId(memberRepository.findById(memberId).orElse(null), bakeryCategory.getBakeryId()).isPresent()) {
                isBooked = Boolean.TRUE;
            } else {
                isBooked = Boolean.FALSE;
            }

            bakeryListResponseDto = BakeryListResponseDto.builder()
                    .bakeryId(bakeryCategory.getBakeryId().getBakeryId())
                    .bakeryName(bakeryCategory.getBakeryId().getBakeryName())
                    .bakeryPicture(bakeryCategory.getBakeryId().getBakeryPicture())
                    .isHACCP(bakeryCategory.getBakeryId().isHACCP())
                    .isVegan(bakeryCategory.getBakeryId().isVegan())
                    .isNonGMO(bakeryCategory.getBakeryId().isNonGMO())
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
}
