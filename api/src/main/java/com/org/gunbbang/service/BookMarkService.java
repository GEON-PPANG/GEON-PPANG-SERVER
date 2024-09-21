package com.org.gunbbang.service;

import com.org.gunbbang.support.exception.DoubleBookMarkRequestException;
import com.org.gunbbang.support.exception.NotFoundException;
import com.org.gunbbang.controller.DTO.response.BookMarkResponseDTO;
import com.org.gunbbang.entity.Bakery;
import com.org.gunbbang.entity.BookMark;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.support.errorType.ErrorType;
import com.org.gunbbang.repository.BakeryRepository;
import com.org.gunbbang.repository.BookMarkRepository;
import com.org.gunbbang.repository.MemberRepository;
import java.util.Optional;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class BookMarkService {

  private final BookMarkRepository bookMarkRepository;
  private final BakeryRepository bakeryRepository;
  private final MemberRepository memberRepository;

  @Retryable(value = ObjectOptimisticLockingFailureException.class, maxAttempts = 100, backoff = @Backoff(delay = 1000))
  public BookMarkResponseDTO doBookMark(boolean isAddingBookMark, Long bakeryId, Long memberId) {
    Bakery foundBakery = bakeryRepository.findById(bakeryId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));

    Member foundMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));

    if (isAddingBookMark) {
      BookMark newBookMark = BookMark.builder()
              .bakery(foundBakery)
              .member(foundMember)
              .build();

      bookMarkRepository.save(newBookMark);
      foundBakery.updateBookMarkCount(true);

      return BookMarkResponseDTO.builder()
              .bookMarkCount(foundBakery.getBookMarkCount())
              .isBookMarked(true)
              .build();
    }

    Optional<BookMark> foundBookMark =
        bookMarkRepository.findByMemberIdAndBakeryId(memberId, bakeryId);

    // 북마크 안했는데 취소한 경우
    if (foundBookMark.isEmpty()) {
      throw new DoubleBookMarkRequestException(
          ErrorType.ALREADY_CANCELED_BOOKMARK_EXCEPTION,
          ErrorType.ALREADY_CANCELED_BOOKMARK_EXCEPTION.getMessage() + "memberId: " + memberId);
    }

    foundBakery.updateBookMarkCount(false);
    bookMarkRepository.deleteByMemberAndBakery(foundMember, foundBakery);
    bookMarkRepository.flush();

    return BookMarkResponseDTO.builder()
            .bookMarkCount(foundBakery.getBookMarkCount())
            .isBookMarked(false)
            .build();
  }
}
