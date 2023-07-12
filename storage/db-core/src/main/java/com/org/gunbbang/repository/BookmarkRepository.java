package com.org.gunbbang.repository;

import com.org.gunbbang.entity.Bakery;
import com.org.gunbbang.entity.BookMark;
import com.org.gunbbang.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookMark, Long> {
    Optional<BookMark> findByMemberAndBakery(Member member, Bakery bakery);
    BookMark saveAndFlush(BookMark bookMark);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM BookMark bm WHERE bm.member = :member AND bm.bakery = :bakery")
    void deleteByMemberAndBakery(Member member, Bakery bakery);

}
