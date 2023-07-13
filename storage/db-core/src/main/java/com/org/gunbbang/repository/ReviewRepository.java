package com.org.gunbbang.repository;

import com.org.gunbbang.entity.Bakery;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    Review save(Review review);
    List<Review> findAllByBakeryOrderByCreatedAtDesc(Bakery bakery);

    List<Review> findAllByMemberOrderByCreatedAtDesc(Member member);
}
