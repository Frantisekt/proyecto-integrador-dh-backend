package com.backend.globeonclick.repository;

import com.backend.globeonclick.entity.MediaPackage;
import com.backend.globeonclick.entity.TourPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ITourPackageRepository extends JpaRepository<TourPackage, Long> {
    @Query(value = "SELECT tp.packageId FROM TourPackage tp",
            countQuery = "SELECT COUNT(tp.packageId) FROM TourPackage tp")
    Page<Long> findAllIds(Pageable pageable);

    @Query("SELECT tp FROM TourPackage tp " +
            "LEFT JOIN FETCH tp.categories " +
            "WHERE tp.packageId = :id")
    Optional<TourPackage> findByIdWithCategories(@Param("id") Long id);

    @Query(value = "SELECT tp.packageId FROM TourPackage tp " +
            "WHERE (:startDate IS NULL OR tp.end_date >= :startDate) " +
            "AND (:endDate IS NULL OR tp.start_date <= :endDate) " +
            "AND (:minPrice IS NULL OR tp.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR tp.price <= :maxPrice)",
            countQuery = "SELECT COUNT(tp.packageId) FROM TourPackage tp " +
                    "WHERE (:startDate IS NULL OR tp.end_date >= :startDate) " +
                    "AND (:endDate IS NULL OR tp.start_date <= :endDate) " +
                    "AND (:minPrice IS NULL OR tp.price >= :minPrice) " +
                    "AND (:maxPrice IS NULL OR tp.price <= :maxPrice)")
    Page<Long> findFilteredIds(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable);
}
