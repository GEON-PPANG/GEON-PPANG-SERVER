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
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BookMarkService {

  private final BookMarkRepository bookMarkRepository;
  private final BakeryRepository bakeryRepository;
  private final MemberRepository memberRepository;

  public BookMarkResponseDTO doBookMark(boolean isAddingBookMark, Long bakeryId, Long memberId) {
    Bakery foundBakery =
        bakeryRepository
            .findById(bakeryId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_BAKERY_EXCEPTION,
                        ErrorType.NOT_FOUND_BAKERY_EXCEPTION.getMessage() + bakeryId));

    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_USER_EXCEPTION,
                        ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId));

    Optional<BookMark> foundBookMark =
        bookMarkRepository.findByMemberIdAndBakeryId(memberId, bakeryId);

    if (isAddingBookMark) {
      // 북마크 했는데 또 한경우
      //      if (foundBookMark.isPresent()) {
      //        throw new DoubleBookMarkRequestException(
      //            ErrorType.ALREADY_BOOKMARKED_EXCEPTION,
      //            ErrorType.ALREADY_BOOKMARKED_EXCEPTION.getMessage()
      //                + "memberId: "
      //                + foundBookMark.get().getMember().getMemberId()
      //                + " bakeryId: "
      //                + foundBookMark.get().getBakery().getBakeryId());
      //      }
      return addBookMark(isAddingBookMark, foundBakery, foundMember);
    }

    // 북마크 안했는데 취소한 경우
    if (foundBookMark.isEmpty()) {
      throw new DoubleBookMarkRequestException(
          ErrorType.ALREADY_CANCELED_BOOKMARK_EXCEPTION,
          ErrorType.ALREADY_CANCELED_BOOKMARK_EXCEPTION.getMessage() + "memberId: " + memberId);
    }
    return cancelBookMark(isAddingBookMark, foundBakery, foundMember);
  }

  private BookMarkResponseDTO cancelBookMark(
      boolean isAddingBookMark, Bakery foundBakery, Member foundMember) {

    updateBookMarkCount(isAddingBookMark, foundBakery);
    bookMarkRepository.deleteByMemberAndBakery(foundMember, foundBakery);
    bookMarkRepository.flush();

    return BookMarkResponseDTO.builder()
        .bookMarkCount(foundBakery.getBookMarkCount())
        .isBookMarked(false)
        .build();
  }

  private BookMarkResponseDTO addBookMark(
      boolean isAddingBookMark, Bakery foundBakery, Member foundMember) {
    try {
      bookMarkRepository.saveAndFlush(
          BookMark.builder().bakery(foundBakery).member(foundMember).build());
    } catch (DataIntegrityViolationException e) {
      throw new DoubleBookMarkRequestException(
          ErrorType.ALREADY_BOOKMARKED_EXCEPTION,
          ErrorType.ALREADY_BOOKMARKED_EXCEPTION.getMessage()
              + "memberId: "
              + foundMember.getMemberId());
    }

    updateBookMarkCount(isAddingBookMark, foundBakery);

    return BookMarkResponseDTO.builder()
        .bookMarkCount(foundBakery.getBookMarkCount())
        .isBookMarked(true)
        .build();
  }

  private void updateBookMarkCount(boolean isAddingBookMark, Bakery foundBakery) {
    bakeryRepository.saveAndFlush(foundBakery);
    foundBakery.updateBookMarkCount(isAddingBookMark);
  }
}
