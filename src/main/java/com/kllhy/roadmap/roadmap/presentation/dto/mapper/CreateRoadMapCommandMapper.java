package com.kllhy.roadmap.roadmap.presentation.dto.mapper;

import com.kllhy.roadmap.roadmap.application.command.dto.*;
import com.kllhy.roadmap.roadmap.presentation.dto.*;
import java.util.*;

public class CreateRoadMapCommandMapper {
    public static CreateRoadMapCommand mapCreateRoadMapCommand(RoadMapCreateRequest request) {
        return new CreateRoadMapCommand(
                request.userId(),
                request.title(),
                request.description(),
                request.isDraft(),
                request.categoryId(),
                Optional.ofNullable(request.topics()).orElseGet(Collections::emptyList).stream()
                        .filter(Objects::nonNull)
                        .map(CreateRoadMapCommandMapper::toCreateTopicCommand)
                        .toList());
    }

    private static CreateTopicCommand toCreateTopicCommand(TopicCreateRequest request) {
        return new CreateTopicCommand(
                request.title(),
                request.content(),
                request.importanceLevel(),
                request.order(),
                request.isDraft(),
                Optional.ofNullable(request.resources()).orElseGet(Collections::emptyList).stream()
                        .filter(Objects::nonNull)
                        .map(CreateRoadMapCommandMapper::toCreateResourceTopicCommand)
                        .toList(),
                Optional.ofNullable(request.subTopics()).orElseGet(Collections::emptyList).stream()
                        .filter(Objects::nonNull)
                        .map(CreateRoadMapCommandMapper::toCreateSubTopicCommand)
                        .toList());
    }

    private static CreateResourceTopicCommand toCreateResourceTopicCommand(
            ResourceTopicCreateRequest request) {
        return new CreateResourceTopicCommand(
                request.name(), request.resourceType(), request.order(), request.link());
    }

    private static CreateSubTopicCommand toCreateSubTopicCommand(SubTopicCreateRequest request) {
        return new CreateSubTopicCommand(
                request.title(),
                request.content(),
                request.importanceLevel(),
                request.isDraft(),
                Optional.ofNullable(request.resources()).orElseGet(Collections::emptyList).stream()
                        .filter(Objects::nonNull)
                        .map(CreateRoadMapCommandMapper::toCreateResourceSubTopicCommand)
                        .toList());
    }

    private static CreateResourceSubTopicCommand toCreateResourceSubTopicCommand(
            ResourceSubTopicCreateRequest request) {
        return new CreateResourceSubTopicCommand(
                request.name(), request.order(), request.resourceType(), request.link());
    }
}
