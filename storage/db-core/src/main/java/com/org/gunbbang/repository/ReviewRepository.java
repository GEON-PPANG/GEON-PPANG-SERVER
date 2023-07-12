package com.org.gunbbang.repository;

import com.org.gunbbang.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    Review save(Review review);
}
