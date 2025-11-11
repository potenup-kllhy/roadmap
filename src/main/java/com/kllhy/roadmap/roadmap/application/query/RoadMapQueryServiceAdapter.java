package com.kllhy.roadmap.roadmap.application.query;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.roadmap.application.query.dto.RoadMapView;
import com.kllhy.roadmap.roadmap.domain.exception.RoadMapIErrorCode;
import com.kllhy.roadmap.roadmap.domain.model.RoadMap;
import com.kllhy.roadmap.roadmap.domain.repository.RoadMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoadMapQueryServiceAdapter implements RoadMapQueryService {
    private final RoadMapRepository roadMapRepository;

    @Override
    public RoadMapView findById(Long id) {
        RoadMap roadMap =
                roadMapRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new DomainException(RoadMapIErrorCode.ROAD_MAP_BAD_REQUEST));
        return null;
    }

    @Override
    public boolean existsById(Long id) {
        return roadMapRepository.existsById(id);
    }
}
