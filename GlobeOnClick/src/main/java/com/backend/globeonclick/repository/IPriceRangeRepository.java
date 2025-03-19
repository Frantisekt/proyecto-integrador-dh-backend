package com.backend.globeonclick.repository;

import com.backend.globeonclick.entity.PriceRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPriceRangeRepository extends JpaRepository<PriceRange, Long> {
    
    @Query("SELECT pr FROM PriceRange pr WHERE :price >= pr.minPrice AND :price < pr.maxPrice")
    Optional<PriceRange> findRangeForPrice(@Param("price") Double price);

    @Query("SELECT DISTINCT pr FROM PriceRange pr WHERE pr.minPrice >= :minPrice AND pr.maxPrice <= :maxPrice")
    List<PriceRange> findRangesInRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

    @Query("SELECT pr FROM PriceRange pr ORDER BY pr.minPrice")
    List<PriceRange> findAllOrderByMinPrice();
} 