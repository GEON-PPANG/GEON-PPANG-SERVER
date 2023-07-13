package com.org.gunbbang.repository;

import com.org.gunbbang.entity.RecommendKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendKeywordRepository extends JpaRepository<RecommendKeyword, Long> {
    Optional<RecommendKeyword> findByKeywordName(String keywordName);

}
