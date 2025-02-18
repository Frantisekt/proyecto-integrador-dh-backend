package com.backend.globeonclick.repository;

import com.backend.globeonclick.entity.TourDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;

@Repository
public interface ITourDateRepository extends JpaRepository<TourDate, Long>{
}
