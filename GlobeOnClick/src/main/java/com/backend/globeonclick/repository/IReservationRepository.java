package com.backend.globeonclick.repository;

import com.backend.globeonclick.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId")
    List<Reservation> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT r FROM Reservation r WHERE r.tourPackage.id = :packageId")
    List<Reservation> findByPackageId(@Param("packageId") Long packageId);
}
