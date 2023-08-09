package com.org.gunbbang.repository;

import com.org.gunbbang.entity.NutrientType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutrientTypeRepository extends JpaRepository<NutrientType, Long> {
  Optional<NutrientType> findByIsNutrientOpenAndIsIngredientOpenAndIsNotOpen(
      Boolean isNutrientOpen, Boolean isIngredientOpen, Boolean isNotOpen);
}
