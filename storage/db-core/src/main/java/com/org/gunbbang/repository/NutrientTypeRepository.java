package com.org.gunbbang.repository;

import com.org.gunbbang.NutrientTypeTag;
import com.org.gunbbang.entity.NutrientType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutrientTypeRepository extends JpaRepository<NutrientType, Long> {
  //  Optional<NutrientType> findByIsNutrientOpenAndIsIngredientOpenAndIsNotOpen(
  //      boolean isNutrientOpen, boolean isIngredientOpen, boolean isNotOpen);

  Optional<NutrientType> findByNutrientTypeTag(NutrientTypeTag nutrientTypeTag);
}
