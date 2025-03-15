package com.backend.globeonclick.repository;

import com.backend.globeonclick.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
    Optional<User> findByDni(String dni);

    @Query("SELECT tp.packageId FROM User u JOIN u.favoritePackages tp WHERE u.userId = :userId")
    Page<Long> findFavoritePackageIds(@Param("userId") Long userId, Pageable pageable);

    boolean existsByUserIdAndFavoritePackagesPackageId(Long userId, Long packageId);
}
