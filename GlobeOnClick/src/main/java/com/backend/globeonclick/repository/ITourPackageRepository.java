package com.backend.globeonclick.repository;

import com.backend.globeonclick.entity.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITourPackageRepository extends JpaRepository<TourPackage, Long> {
}
