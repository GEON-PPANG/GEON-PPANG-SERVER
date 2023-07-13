package com.org.gunbbang.service;

import com.org.gunbbang.DoubleBookMarkRequestException;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.controller.DTO.response.BookMarkResponseDTO;
import com.org.gunbbang.entity.Bakery;
import com.org.gunbbang.entity.BookMark;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.BakeryRepository;
import com.org.gunbbang.repository.BookMarkRepository;
import com.org.gunbbang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final BakeryRepository bakeryRepository;
    private final MemberRepository memberRepository;

    public BookMarkResponseDTO doBookMark(
            boolean isAddingBookMark,
            Long bakeryId,
            Long memberId
    ) {
        System.out.println("isAddingbookMark 값: " + isAddingBookMark);
        Bakery foundBakery = bakeryRepository
                .findById(bakeryId)
                .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));

        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));

        Optional<BookMark> foundBookMark = bookMarkRepository.findByMemberAndBakery(foundMember, foundBakery);

        if (isAddingBookMark) {
            if(foundBookMark.isPresent()) {  // 북마크 했는데 또 한경우
                System.out.println("북마크 객체 결과: " + foundBookMark.get().getBookMarkId());
                System.out.println("북마크 멤버 결과: " + foundBookMark.get().getMember().getMemberId());
                System.out.println("북마크 빵집 결과: " + foundBookMark.get().getBakery().getBakeryId());

                throw new DoubleBookMarkRequestException(ErrorType.ALREADY_BOOKMARKED_EXCEPTION);
            }
            bookMarkRepository.saveAndFlush(
                    BookMark.builder()
                            .bakery(foundBakery)
                            .member(foundMember).build());

            foundBakery.updateBookMarkCount(isAddingBookMark);
            bakeryRepository.saveAndFlush(foundBakery);
            // flush 안되는 문제 해결
            return BookMarkResponseDTO.builder()
                    .bookMarkCount(foundBakery.getBookMarkCount())
                    .build();
        }

        // 북마크 취소 요청으로 왔는데 사실 북마크 안되어있는 경우
        if (foundBookMark.isEmpty()) {
            throw new DoubleBookMarkRequestException(ErrorType.ALREADY_CANCELED_BOOKMARK_EXCEPTION);
        }

        bookMarkRepository.deleteByMemberAndBakery(foundMember, foundBakery);

        foundBakery.updateBookMarkCount(isAddingBookMark);
        bakeryRepository.saveAndFlush(foundBakery);
        return BookMarkResponseDTO.builder()
                .bookMarkCount(foundBakery.getBookMarkCount())
                .build();
    }
}
