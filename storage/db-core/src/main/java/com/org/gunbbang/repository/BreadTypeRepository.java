package com.org.gunbbang.repository;

import com.org.gunbbang.entity.BreadType;
import org.aspectj.apache.bcel.util.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BreadTypeRepository extends JpaRepository<BreadType, Long> {
    Optional<BreadType> findBreadTypeByIsGlutenFreeAndIsVeganAndIsNutFreeAndIsSugarFree(
            Boolean isGlutenFree, Boolean isVegan, Boolean isNutFree, Boolean isSugarFree
    );
}
