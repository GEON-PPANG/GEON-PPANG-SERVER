package com.org.gunbbang.repository;

import com.org.gunbbang.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    Optional<Review> save(Review review);
}
