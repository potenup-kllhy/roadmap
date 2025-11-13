package com.kllhy.roadmap.star.roadmap.domain.service;

import com.kllhy.roadmap.common.annotation.DomainService;
import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.star.roadmap.domain.exception.StarRoadMapErrorCode;
import com.kllhy.roadmap.star.roadmap.domain.model.command.DeleteStarRoadMapCommand;
import com.kllhy.roadmap.star.roadmap.domain.repository.StarRoadMapRepository;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class StarRoadMapDeletionService {

    private final StarRoadMapRepository starRoadMapRepository;

    public void deleteByUserIdAndRoadmapId(DeleteStarRoadMapCommand command) {
        if (!starRoadMapRepository.existsByUserIdAndRoadmapId(
                command.userId(), command.roadmapId())) {
            throw new DomainException(StarRoadMapErrorCode.STAR_ROAD_MAP_NOT_FOUND);
        }
        starRoadMapRepository.deleteByUserIdAndRoadmapId(command.userId(), command.roadmapId());
    }
}
