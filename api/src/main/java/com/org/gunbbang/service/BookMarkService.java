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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookMarkService {

  private final BookMarkRepository bookMarkRepository;
  private final BakeryRepository bakeryRepository;
  private final MemberRepository memberRepository;

  public BookMarkResponseDTO doBookMark(boolean isAddingBookMark, Long bakeryId, Long memberId) {
    Bakery foundBakery =
        bakeryRepository
            .findById(bakeryId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));

    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));

    Optional<BookMark> foundBookMark =
        bookMarkRepository.findByMemberAndBakery(foundMember, foundBakery);

    if (isAddingBookMark) {
      if (foundBookMark.isPresent()) { // 북마크 했는데 또 한경우
        throw new DoubleBookMarkRequestException(ErrorType.ALREADY_BOOKMARKED_EXCEPTION);
      }
      bookMarkRepository.saveAndFlush(
          BookMark.builder().bakery(foundBakery).member(foundMember).build());

      foundBakery.updateBookMarkCount(isAddingBookMark);
      bakeryRepository.saveAndFlush(foundBakery);
      // flush 안되는 문제 해결
      return BookMarkResponseDTO.builder()
          .bookMarkCount(foundBakery.getBookMarkCount())
          .isBookMarked(true)
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
        .isBookMarked(false)
        .build();
  }
}
