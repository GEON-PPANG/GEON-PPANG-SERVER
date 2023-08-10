package com.org.gunbbang.repository;

import com.org.gunbbang.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByCategoryName(String categoryName);
}
