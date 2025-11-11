package com.kllhy.roadmap.roadmap.application.query;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.roadmap.application.query.dto.*;
import com.kllhy.roadmap.roadmap.domain.exception.RoadMapIErrorCode;
import com.kllhy.roadmap.roadmap.domain.model.RoadMap;
import com.kllhy.roadmap.roadmap.domain.repository.RoadMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoadMapQueryServiceAdapter implements RoadMapQueryService {
    private final RoadMapRepository roadMapRepository;

    @Override
    public RoadMapView findById(Long id) {
        RoadMap roadMap =
                roadMapRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new DomainException(RoadMapIErrorCode.ROAD_MAP_BAD_REQUEST));
        return new RoadMapView(
                roadMap.getId(),
                roadMap.getTitle(),
                roadMap.getDescription(),
                roadMap.getDeletedAt(),
                roadMap.getCreatedAt(),
                roadMap.getModifiedAt(),
                roadMap.isDeleted(),
                roadMap.isDraft(),
                roadMap.getCategoryId(),
                roadMap.getTopics().stream()
                        .map(
                                topic ->
                                        new TopicView(
                                                topic.getId(),
                                                topic.getResources().stream()
                                                        .map(
                                                                resourceTopic ->
                                                                        new ResourceTopicView(
                                                                                resourceTopic
                                                                                        .getId(),
                                                                                resourceTopic
                                                                                        .getName(),
                                                                                resourceTopic
                                                                                        .getResourceType(),
                                                                                resourceTopic
                                                                                        .getOrder(),
                                                                                resourceTopic
                                                                                        .getLink()))
                                                        .toList(),
                                                topic.getSubTopics().stream()
                                                        .map(
                                                                subTopic ->
                                                                        new SubTopicView(
                                                                                subTopic.getId(),
                                                                                subTopic.getTitle(),
                                                                                subTopic
                                                                                        .getContent(),
                                                                                subTopic
                                                                                        .getImportanceLevel(),
                                                                                subTopic
                                                                                        .getDeletedAt(),
                                                                                subTopic
                                                                                        .getIsDraft(),
                                                                                subTopic
                                                                                        .getIsDeleted(),
                                                                                subTopic
                                                                                        .getResources()
                                                                                        .stream()
                                                                                        .map(
                                                                                                resourceSubTopic ->
                                                                                                        new ResourceSubTopicView(
                                                                                                                resourceSubTopic
                                                                                                                        .getId(),
                                                                                                                resourceSubTopic
                                                                                                                        .getName(),
                                                                                                                resourceSubTopic
                                                                                                                        .getResourceType(),
                                                                                                                resourceSubTopic
                                                                                                                        .getOrder(),
                                                                                                                resourceSubTopic
                                                                                                                        .getLink()))
                                                                                        .toList(),
                                                                                subTopic
                                                                                        .getCreatedAt(),
                                                                                subTopic
                                                                                        .getModifiedAt()))
                                                        .toList(),
                                                topic.getTitle(),
                                                topic.getContent(),
                                                topic.getImportanceLevel(),
                                                topic.getOrder(),
                                                topic.getCreatedAt(),
                                                topic.getModifiedAt(),
                                                topic.getDeletedAt(),
                                                topic.isDraft(),
                                                topic.isDeleted()))
                        .toList());
    }

    @Override
    public boolean existsById(Long id) {
        return roadMapRepository.existsById(id);
    }
}
