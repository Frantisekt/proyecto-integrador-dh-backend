package com.backend.globeonclick.repository;

import com.backend.globeonclick.entity.MediaPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMediaPackageRepository extends JpaRepository<MediaPackage, Long> {
}

