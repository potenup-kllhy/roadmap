package com.kllhy.roadmap.roadmap.application.command;

import com.kllhy.roadmap.category.application.query.CategoryQueryService;
import com.kllhy.roadmap.category.domain.exception.CategoryErrorCode;
import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.roadmap.application.command.dto.CreateRoadMapCommand;
import com.kllhy.roadmap.roadmap.application.command.dto.mapper.CreationRoadMapMapper;
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
    private final CategoryQueryService categoryQueryService;

    @Override
    public long createRoadMap(CreateRoadMapCommand command) {
        boolean isNotExists = !categoryQueryService.categoryExists(command.categoryId());
        if (isNotExists) {
            throw new DomainException(CategoryErrorCode.CATEGORY_NOT_FOUND);
        }
        RoadMap roadMap = RoadMap.create(CreationRoadMapMapper.toCreationRoadMap(command));
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
}
