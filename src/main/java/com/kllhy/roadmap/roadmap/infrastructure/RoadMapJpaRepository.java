package com.kllhy.roadmap.roadmap.infrastructure;

import com.kllhy.roadmap.roadmap.domain.model.RoadMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadMapJpaRepository extends JpaRepository<RoadMap, Long> {}
