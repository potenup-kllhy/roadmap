package com.kllhy.roadmap.star.roadmap.application.query;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.star.roadmap.application.query.dto.StarRoadMapView;
import com.kllhy.roadmap.star.roadmap.domain.exception.StarRoadMapErrorCode;
import com.kllhy.roadmap.star.roadmap.domain.model.StarRoadMap;
import com.kllhy.roadmap.star.roadmap.domain.repository.StarRoadMapRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StarRoadMapQueryServiceAdapter implements StarRoadMapQueryService {

    private final StarRoadMapRepository starRoadMapRepository;

    @Override
    public StarRoadMapView getById(Long starRoadMapId) {
        return starRoadMapRepository
                .findById(starRoadMapId)
                .map(this::toView)
                .orElseThrow(() -> new DomainException(StarRoadMapErrorCode.STAR_ROAD_MAP_NOT_FOUND));
    }

    @Override
    public List<StarRoadMapView> getAllStarByUserId(Long userId) {
        return starRoadMapRepository.findByUserId(userId)
                .stream()
                .map(this::toView)
                .toList();
    }

    @Override
    public List<StarRoadMapView> getAllStarByRoadMapId(Long roadMapId) {
        return starRoadMapRepository.findByRoadmapId(roadMapId)
                .stream()
                .map(this::toView)
                .toList();
    }

    private StarRoadMapView toView(StarRoadMap starRoadMap) {
        return new StarRoadMapView(
                starRoadMap.getUserId(),
                starRoadMap.getRoadMapId(),
                starRoadMap.getValue()
        );
    }
}
