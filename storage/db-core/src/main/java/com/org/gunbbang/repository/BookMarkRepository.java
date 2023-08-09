package com.org.gunbbang.repository;

import com.org.gunbbang.entity.Bakery;
import com.org.gunbbang.entity.BookMark;
import com.org.gunbbang.entity.Member;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
  Optional<BookMark> findByMemberAndBakery(Member member, Bakery bakery);

  @Query(
      value =
          "SELECT bm FROM BookMark bm WHERE bm.member.memberId = :memberId AND bm.bakery.bakeryId ="
              + " :bakeryId")
  Optional<BookMark> findByMemberIdAndBakeryId(Long memberId, Long bakeryId);

  BookMark saveAndFlush(BookMark bookMark);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM BookMark bm WHERE bm.member = :member AND bm.bakery = :bakery")
  void deleteByMemberAndBakery(Member member, Bakery bakery);
}
