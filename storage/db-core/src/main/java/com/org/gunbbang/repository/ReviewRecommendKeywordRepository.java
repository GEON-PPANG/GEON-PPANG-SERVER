package com.org.gunbbang.repository;

import com.org.gunbbang.entity.ReviewRecommendKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRecommendKeywordRepository extends JpaRepository<ReviewRecommendKeyword, Long> {
    Optional<ReviewRecommendKeyword> save(ReviewRecommendKeyword reviewRecommendKeyword);
}
