package com.kllhy.roadmap.star.roadmap.domain.service;

import com.kllhy.roadmap.common.annotation.DomainService;
import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.roadmap.application.query.RoadMapQueryService;
import com.kllhy.roadmap.roadmap.application.query.dto.RoadMapView;
import com.kllhy.roadmap.star.roadmap.domain.exception.StarRoadMapErrorCode;
import com.kllhy.roadmap.star.roadmap.domain.model.StarRoadMap;
import com.kllhy.roadmap.star.roadmap.domain.model.command.CreateStarRoadMapCommand;
import com.kllhy.roadmap.star.roadmap.domain.repository.StarRoadMapRepository;
import com.kllhy.roadmap.user.domain.enums.AccountStatus;
import com.kllhy.roadmap.user.service.UserService;
import com.kllhy.roadmap.user.service.view.UserView;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class StarRoadMapCreationService {

    private final UserService userService;
    private final RoadMapQueryService roadMapQueryService;
    private final StarRoadMapRepository starRoadMapRepository;

    public StarRoadMap create(CreateStarRoadMapCommand command) {
        if (starRoadMapRepository.existsByUserIdAndRoadmapId(
                command.userId(), command.roadmapId())) {
            throw new DomainException(StarRoadMapErrorCode.STAR_ROAD_MAP_ALREADY_EXISTS);
        }

        UserView user = userService.getByUser(command.userId());
        if (user.status() != AccountStatus.ACTIVE) {
            throw new DomainException(StarRoadMapErrorCode.STAR_ROAD_MAP_USER_NOT_ACTIVE);
        }

        RoadMapView roadMap = roadMapQueryService.findById(command.roadmapId());
        if (roadMap.isDraft() || roadMap.isDeleted()) {
            throw new DomainException(StarRoadMapErrorCode.STAR_ROAD_MAP_ROADMAP_INVALID);
        }

        return StarRoadMap.create(command);
    }
}
