package com.org.gunbbang.repository;

import com.org.gunbbang.entity.BakeryNutrientType;
import com.org.gunbbang.entity.NutrientType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BakeryNutrientTypeRepository extends JpaRepository<BakeryNutrientType, Long> {
  List<BakeryNutrientType> findAllByNutrientType(NutrientType nutrientType);
}
