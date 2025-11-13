package com.kllhy.roadmap.roadmap.application.command;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.roadmap.domain.exception.RoadMapIErrorCode;
import com.kllhy.roadmap.roadmap.domain.model.RoadMap;
import com.kllhy.roadmap.roadmap.domain.model.update_spec.UpdateRoadMap;
import com.kllhy.roadmap.roadmap.domain.repository.RoadMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoadMapCommandServiceAdapter implements RoadMapCommandService {

    private final RoadMapRepository roadMapRepository;

    @Override
    public void update(long roadMapId, UpdateRoadMap updateRoadMap) {
        RoadMap roadMap =
                roadMapRepository
                        .findById(roadMapId)
                        .orElseThrow(
                                () -> new DomainException(RoadMapIErrorCode.ROAD_MAP_BAD_REQUEST));
        roadMap.update(updateRoadMap);
    }
}
