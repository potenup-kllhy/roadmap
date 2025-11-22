package com.kllhy.roadmap.star.roadmap.domain.service;

import com.kllhy.roadmap.common.annotation.DomainService;
import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.star.roadmap.domain.exception.StarRoadMapErrorCode;
import com.kllhy.roadmap.star.roadmap.domain.model.StarRoadMap;
import com.kllhy.roadmap.star.roadmap.domain.model.command.UpdateStarRoadMapCommand;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class StarRoadMapUpdateService {

    public void update(StarRoadMap star, UpdateStarRoadMapCommand command) {
        if (!star.isOwner(command.userId())) {
            throw new DomainException(StarRoadMapErrorCode.STAR_ROAD_MAP_NOT_AUTHORIZED);
        }
        star.update(command.newValue());
    }
}
