package com.kllhy.roadmap.travel.infrastructure.jpa;

import com.kllhy.roadmap.travel.domain.model.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelJpaRepository extends JpaRepository<Travel, Long> {}
