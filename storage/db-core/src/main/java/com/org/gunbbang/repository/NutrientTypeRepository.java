package com.org.gunbbang.repository;

import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.NutrientType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NutrientTypeRepository extends JpaRepository<NutrientType, Long> {
    Optional<NutrientType> findByIsNutrientOpenAndIsIngredientOpenAndIsNotOpen(
            Boolean isNutrientOpen, Boolean isIngredientOpen, Boolean isNotOpen
    );
}
