package com.backend.globeonclick.repository;

import com.backend.globeonclick.entity.MediaPackage;
import com.backend.globeonclick.entity.TourPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
