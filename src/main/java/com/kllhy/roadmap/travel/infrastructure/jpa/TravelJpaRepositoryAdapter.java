package com.kllhy.roadmap.travel.infrastructure.jpa;

import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.repository.TravelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@AllArgsConstructor
public class TravelJpaRepositoryAdapter implements TravelRepository {

    private final TravelJpaRepository travelJpaRepository;

    @Override
    public Optional<Travel> findById(Long id) {
        return travelJpaRepository.findById(id);
    }
}
