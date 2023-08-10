package com.org.gunbbang.repository;

import com.org.gunbbang.entity.RecommendKeyword;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendKeywordRepository extends JpaRepository<RecommendKeyword, Long> {
  Optional<RecommendKeyword> findByKeywordName(String keywordName);
}
