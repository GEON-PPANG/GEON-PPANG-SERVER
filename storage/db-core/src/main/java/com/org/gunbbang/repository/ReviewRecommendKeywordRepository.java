package com.org.gunbbang.repository;

import com.org.gunbbang.entity.Review;
import com.org.gunbbang.entity.ReviewRecommendKeyword;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRecommendKeywordRepository
    extends JpaRepository<ReviewRecommendKeyword, Long> {
  List<ReviewRecommendKeyword> findAllByReview(Review review);
}
