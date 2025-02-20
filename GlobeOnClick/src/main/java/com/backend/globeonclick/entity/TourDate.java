package com.backend.globeonclick.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tour_dates")
public class TourDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dateId;

    private String title;
    private String startDate;
    private String endDate;
    private String recommendations;
    private boolean state;
    private Integer level;

    @OneToMany(mappedBy = "tourDate")
    private List<Reservation> reservations;
} 