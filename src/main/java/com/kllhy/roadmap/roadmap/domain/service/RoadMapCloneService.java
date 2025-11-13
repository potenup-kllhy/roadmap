package com.kllhy.roadmap.roadmap.domain.service;

import com.kllhy.roadmap.common.annotation.DomainService;
import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.roadmap.domain.exception.RoadMapIErrorCode;
import com.kllhy.roadmap.roadmap.domain.model.RoadMap;
import com.kllhy.roadmap.user.domain.enums.AccountStatus;
import com.kllhy.roadmap.user.service.view.UserView;

@DomainService
public class RoadMapCloneService {
    public RoadMap cloneAsIs(UserView user, boolean isCategoryExists, RoadMap roadMap) {
        if (!user.status().equals(AccountStatus.ACTIVE)) {
            throw new DomainException(RoadMapIErrorCode.ROAD_MAP_USER_NOT_ACTIVE);
        }

        if (!isCategoryExists) {
            throw new DomainException(RoadMapIErrorCode.ROAD_MAP_CATEGORY_NOT_FOUND);
        }

        return roadMap.cloneAsIs(user.id());
    }
}
