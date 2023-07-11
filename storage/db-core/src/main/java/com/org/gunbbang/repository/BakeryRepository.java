package com.org.gunbbang.repository;

import com.org.gunbbang.entity.Bakery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BakeryRepository extends JpaRepository<Bakery, Long> {
    Optional<Bakery> findById(Long Id);
}
