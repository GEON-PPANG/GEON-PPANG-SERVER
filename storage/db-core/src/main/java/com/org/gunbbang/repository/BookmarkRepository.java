package com.org.gunbbang.repository;

import com.org.gunbbang.entity.Bakery;
import com.org.gunbbang.entity.BookMark;
import com.org.gunbbang.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookMark, Long> {
    Optional<BookMark> findByMemberIdAndBakeryId(Member memberId, Bakery bakeryId);
}
