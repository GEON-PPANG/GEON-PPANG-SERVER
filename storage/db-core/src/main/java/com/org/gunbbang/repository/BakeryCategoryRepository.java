package com.org.gunbbang.repository;

import com.org.gunbbang.entity.BakeryCategory;
import com.org.gunbbang.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BakeryCategoryRepository extends JpaRepository<BakeryCategory,Long> {
    @Query("SELECT bc FROM BakeryCategory bc WHERE bc.categoryId IN (:categoryList) order by bc.bakeryId.bakeryId desc")
    List<BakeryCategory> findByBakeryCategoryId(@Param("categoryList") List<Category> categoryList);

    @Query("SELECT bc FROM BakeryCategory bc WHERE bc.categoryId IN (:categoryList) order by bc.bakeryId.reviewCount desc")
    List<BakeryCategory> findByBakeryCategoryIdAndReview(@Param("categoryList") List<Category> categoryList);

}
