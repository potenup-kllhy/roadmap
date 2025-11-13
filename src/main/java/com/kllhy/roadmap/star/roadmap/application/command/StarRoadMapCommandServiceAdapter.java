package com.kllhy.roadmap.star.roadmap.application.command;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.star.roadmap.domain.exception.StarRoadMapErrorCode;
import com.kllhy.roadmap.star.roadmap.domain.model.StarRoadMap;
import com.kllhy.roadmap.star.roadmap.domain.model.command.CreateStarRoadMapCommand;
import com.kllhy.roadmap.star.roadmap.domain.model.command.UpdateStarRoadMapCommand;
import com.kllhy.roadmap.star.roadmap.domain.repository.StarRoadMapRepository;
import com.kllhy.roadmap.star.roadmap.domain.service.StarRoadMapCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StarRoadMapCommandServiceAdapter implements StarRoadMapCommandService {
    private final StarRoadMapRepository starRoadMapRepository;
    private final StarRoadMapCreationService starRoadMapCreationService;

    @Override
    public Long create(CreateStarRoadMapCommand command) {
        StarRoadMap starRoadMap = starRoadMapCreationService.create(command);
        StarRoadMap savedStarRoadMap = starRoadMapRepository.save(starRoadMap);
        return savedStarRoadMap.getId();
    }

    @Override
    public void update(UpdateStarRoadMapCommand command) {
        StarRoadMap starRoadMap =
                starRoadMapRepository
                        .findById(command.starRoadMapId())
                        .orElseThrow(
                                () ->
                                        new DomainException(
                                                StarRoadMapErrorCode.STAR_ROAD_MAP_NOT_FOUND));

        if (!starRoadMap.getUserId().equals(command.userId())) {
            throw new DomainException(StarRoadMapErrorCode.STAR_ROAD_MAP_NOT_AUTHORIZED);
        }

        starRoadMap.update(command.value());
    }

    @Override
    public void deleteById(Long starId) {
        starRoadMapRepository.deleteById(starId);
    }

    @Override
    public void deleteAllStarByUserId(Long userId) {
        starRoadMapRepository.deleteAllByUserId(userId);
    }

    @Override
    public void deleteAllStarByRoadMapId(Long roadmapId) {
        starRoadMapRepository.deleteAllByRoadmapId(roadmapId);
    }

    @Override
    public void deleteByUserIdAndRoadmapId(Long userId, Long roadmapId) {
        starRoadMapRepository.deleteByUserIdAndRoadmapId(userId, roadmapId);
    }
}
