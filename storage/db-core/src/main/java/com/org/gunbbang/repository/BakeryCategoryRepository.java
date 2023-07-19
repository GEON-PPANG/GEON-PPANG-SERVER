package com.org.gunbbang.repository;

import com.org.gunbbang.entity.BakeryCategory;
import com.org.gunbbang.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BakeryCategoryRepository extends JpaRepository<BakeryCategory,Long> {
    List<BakeryCategory> findDistinctByCategoryIn(List<Category> categoryList, Sort sort);

}
