package com.kllhy.roadmap.roadmap.application.command;

import com.kllhy.roadmap.category.application.query.CategoryQueryService;
import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.roadmap.application.command.dto.CreateRoadMapCommand;
import com.kllhy.roadmap.roadmap.application.command.dto.mapper.CreationRoadMapMapper;
import com.kllhy.roadmap.roadmap.domain.exception.RoadMapIErrorCode;
import com.kllhy.roadmap.roadmap.domain.model.RoadMap;
import com.kllhy.roadmap.roadmap.domain.model.update_spec.UpdateRoadMap;
import com.kllhy.roadmap.roadmap.domain.repository.RoadMapRepository;
import com.kllhy.roadmap.roadmap.domain.service.RoadMapCloneService;
import com.kllhy.roadmap.roadmap.domain.service.RoadMapCreationService;
import com.kllhy.roadmap.user.service.UserService;
import com.kllhy.roadmap.user.service.view.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoadMapCommandServiceAdapter implements RoadMapCommandService {

    private final RoadMapRepository roadMapRepository;
    private final CategoryQueryService categoryQueryService;
    private final UserService userService;
    private final RoadMapCreationService roadMapCreationService;
    private final RoadMapCloneService roadMapCloneService;

    @Override
    @Transactional
    public long createRoadMap(CreateRoadMapCommand command) {
        UserView user = userService.getByUser(command.userId());
        boolean isCategoryExists = categoryQueryService.categoryExists(command.categoryId());
        RoadMap roadMap =
                roadMapCreationService.create(
                        user, isCategoryExists, CreationRoadMapMapper.toCreationRoadMap(command));
        return roadMapRepository.save(roadMap);
    }

    @Override
    public void update(long roadMapId, UpdateRoadMap updateRoadMap) {
        RoadMap roadMap =
                roadMapRepository
                        .findById(roadMapId)
                        .orElseThrow(
                                () -> new DomainException(RoadMapIErrorCode.ROAD_MAP_BAD_REQUEST));
        roadMap.update(updateRoadMap);
    }

    @Transactional
    public long cloneRoadMap(long userId, long roadMapId, long categoryId) {
        UserView user = userService.getByUser(userId);
        boolean isCategoryExists = categoryQueryService.categoryExists(categoryId);
        RoadMap roadMap =
                roadMapRepository
                        .findById(roadMapId)
                        .orElseThrow(
                                () -> new DomainException(RoadMapIErrorCode.ROAD_MAP_NOT_FOUND));
        RoadMap clonedRoadMap = roadMapCloneService.cloneAsIs(user, isCategoryExists, roadMap);
        return roadMapRepository.save(clonedRoadMap);
    }
}
