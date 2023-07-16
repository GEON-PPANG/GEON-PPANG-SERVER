package com.org.gunbbang.repository;

import com.org.gunbbang.entity.BakeryCategory;
import com.org.gunbbang.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BakeryCategoryRepository extends JpaRepository<BakeryCategory,Long> {
    @Query("SELECT bc FROM BakeryCategory bc WHERE bc.category IN (:categoryList) order by bc.bakery.bakeryId desc")
    List<BakeryCategory> findByBakeryCategory(@Param("categoryList") List<Category> categoryList);

    @Query("SELECT bc FROM BakeryCategory bc WHERE bc.category IN (:categoryList) order by bc.bakery.reviewCount desc")
    List<BakeryCategory> findByBakeryCategoryAndReview(@Param("categoryList") List<Category> categoryList);

}
