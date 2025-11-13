package com.kllhy.roadmap.star.roadmap.application.command;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.roadmap.application.query.RoadMapQueryService;
import com.kllhy.roadmap.roadmap.application.query.dto.RoadMapView;
import com.kllhy.roadmap.star.roadmap.domain.exception.StarRoadMapErrorCode;
import com.kllhy.roadmap.star.roadmap.domain.model.StarRoadMap;
import com.kllhy.roadmap.star.roadmap.domain.model.command.CreateStarRoadMapCommand;
import com.kllhy.roadmap.star.roadmap.domain.model.command.DeleteStarRoadMapCommand;
import com.kllhy.roadmap.star.roadmap.domain.model.command.UpdateStarRoadMapCommand;
import com.kllhy.roadmap.star.roadmap.domain.repository.StarRoadMapRepository;
import com.kllhy.roadmap.star.roadmap.domain.service.StarRoadMapCreationService;
import com.kllhy.roadmap.star.roadmap.domain.service.StarRoadMapDeletionService;
import com.kllhy.roadmap.star.roadmap.domain.service.StarRoadMapUpdateService;
import com.kllhy.roadmap.user.domain.enums.AccountStatus;
import com.kllhy.roadmap.user.service.UserService;
import com.kllhy.roadmap.user.service.view.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StarRoadMapCommandServiceAdapter implements StarRoadMapCommandService {

    private final UserService userService;
    private final RoadMapQueryService roadMapQueryService;
    private final StarRoadMapCreationService starRoadMapCreationService;
    private final StarRoadMapUpdateService starRoadMapUpdateService;
    private final StarRoadMapDeletionService starRoadMapDeletionService;
    private final StarRoadMapRepository starRoadMapRepository;

    @Override
    public Long create(Long userId, Long roadmapId, int value) {
        validateUserAndRoadMapStatus(userId, roadmapId);
        CreateStarRoadMapCommand command = new CreateStarRoadMapCommand(userId, roadmapId, value);
        StarRoadMap starRoadMap = starRoadMapCreationService.create(command);
        StarRoadMap savedStarRoadMap = starRoadMapRepository.save(starRoadMap);
        return savedStarRoadMap.getId();
    }

    @Override
    public void update(Long starRoadmapId, Long userId, int value) {
        UpdateStarRoadMapCommand command = new UpdateStarRoadMapCommand(userId, starRoadmapId, value);

        StarRoadMap starRoadMap =
                starRoadMapRepository
                        .findById(starRoadmapId)
                        .orElseThrow(
                                () ->
                                        new DomainException(
                                                StarRoadMapErrorCode.STAR_ROAD_MAP_NOT_FOUND));

        validateUserAndRoadMapStatus(userId, starRoadMap.getRoadMapId());
        starRoadMapUpdateService.update(starRoadMap, command);
        starRoadMapRepository.save(starRoadMap);
    }

    @Override
    public void deleteByUserIdAndRoadmapId(Long userId, Long roadmapId) {
        validateUserAndRoadMapStatus(userId, roadmapId);
        DeleteStarRoadMapCommand command = new DeleteStarRoadMapCommand(userId, roadmapId);
        starRoadMapDeletionService.deleteByUserIdAndRoadmapId(command);
    }

    @Override
    public void deleteAllStarByUserId(Long userId) {
        starRoadMapRepository.deleteAllByUserId(userId);
    }

    @Override
    public void deleteAllStarByRoadMapId(Long roadmapId) {
        starRoadMapRepository.deleteAllByRoadmapId(roadmapId);
    }

    private void validateUserAndRoadMapStatus(Long userId, Long roadmapId) {
        UserView user = userService.getByUser(userId);
        if (user.status() != AccountStatus.ACTIVE) {
            throw new DomainException(StarRoadMapErrorCode.STAR_ROAD_MAP_USER_NOT_ACTIVE);
        }

        RoadMapView roadMap = roadMapQueryService.findById(roadmapId);
        if (roadMap.isDraft() || roadMap.isDeleted()) {
            throw new DomainException(StarRoadMapErrorCode.STAR_ROAD_MAP_ROADMAP_INVALID);
        }
    }
}
