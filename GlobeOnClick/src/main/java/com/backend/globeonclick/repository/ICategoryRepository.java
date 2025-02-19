package com.backend.globeonclick.repository;

import com.backend.globeonclick.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByTourPackagesPackageId(Long tourPackageId);
}
