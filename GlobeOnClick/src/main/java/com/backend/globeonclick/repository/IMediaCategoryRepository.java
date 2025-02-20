package com.backend.globeonclick.repository;

import com.backend.globeonclick.entity.MediaCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMediaCategoryRepository extends JpaRepository<MediaCategory, Long> {
}
