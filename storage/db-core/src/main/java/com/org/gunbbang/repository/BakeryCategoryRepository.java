package com.org.gunbbang.repository;

import com.org.gunbbang.entity.BakeryCategory;
import com.org.gunbbang.entity.Category;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BakeryCategoryRepository extends JpaRepository<BakeryCategory, Long> {

  List<BakeryCategory> findDistinctByCategoryIn(List<Category> categoryList, Sort sort);
}
