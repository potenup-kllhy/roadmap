package com.kllhy.roadmap.travel.infrastructure.jpa;

import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.repository.TravelRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class TravelJpaRepositoryAdapter implements TravelRepository {

    private final TravelJpaRepository travelJpaRepository;

    @Override
    public Optional<Travel> findById(Long id) {
        return travelJpaRepository.findById(id);
    }
}
