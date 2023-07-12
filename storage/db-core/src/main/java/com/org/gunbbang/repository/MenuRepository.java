package com.org.gunbbang.repository;

import com.org.gunbbang.entity.Bakery;
import com.org.gunbbang.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByBakery(Bakery bakery);
}
