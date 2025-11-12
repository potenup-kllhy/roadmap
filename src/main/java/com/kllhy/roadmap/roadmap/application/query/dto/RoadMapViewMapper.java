package com.kllhy.roadmap.roadmap.application.query.dto;

import com.kllhy.roadmap.roadmap.domain.model.*;
import java.util.ArrayList;
import java.util.List;

public final class RoadMapViewMapper {
    public static RoadMapView toView(RoadMap roadMap) {
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
                mapTopics(roadMap.getTopics()));
    }

    private static List<TopicView> mapTopics(List<Topic> topics) {
        if (topics == null || topics.isEmpty()) {
            return List.of();
        }
        List<TopicView> topicViews = new ArrayList<>(topics.size());
        for (Topic topic : topics) {
            topicViews.add(toTopicView(topic));
        }
        return List.copyOf(topicViews);
    }

    private static TopicView toTopicView(Topic topic) {
        return new TopicView(
                topic.getId(),
                mapResourceTopics(topic.getResources()),
                mapSubTopics(topic.getSubTopics()),
                topic.getTitle(),
                topic.getContent(),
                topic.getImportanceLevel(),
                topic.getOrder(),
                topic.getCreatedAt(),
                topic.getModifiedAt(),
                topic.getDeletedAt(),
                topic.isDraft(),
                topic.isDeleted());
    }

    private static List<ResourceTopicView> mapResourceTopics(List<ResourceTopic> resourceTopics) {
        if (resourceTopics == null || resourceTopics.isEmpty()) {
            return List.of();
        }

        List<ResourceTopicView> resourceTopicViews = new ArrayList<>(resourceTopics.size());
        for (ResourceTopic resourceTopic : resourceTopics) {
            resourceTopicViews.add(toResourceTopicView(resourceTopic));
        }
        return resourceTopicViews;
    }

    private static ResourceTopicView toResourceTopicView(ResourceTopic resourceTopic) {
        return new ResourceTopicView(
                resourceTopic.getId(),
                resourceTopic.getName(),
                resourceTopic.getResourceType(),
                resourceTopic.getOrder(),
                resourceTopic.getLink());
    }

    private static List<SubTopicView> mapSubTopics(List<SubTopic> subTopics) {
        if (subTopics == null || subTopics.isEmpty()) {
            return List.of();
        }

        List<SubTopicView> subTopicViews = new ArrayList<>(subTopics.size());
        for (SubTopic subTopic : subTopics) {
            subTopicViews.add(toSubTopicView(subTopic));
        }
        return subTopicViews;
    }

    private static SubTopicView toSubTopicView(SubTopic subTopic) {
        return new SubTopicView(
                subTopic.getId(),
                subTopic.getTitle(),
                subTopic.getContent(),
                subTopic.getImportanceLevel(),
                subTopic.getDeletedAt(),
                subTopic.getIsDraft(),
                subTopic.getIsDeleted(),
                mapResourceSubTopics(subTopic.getResources()),
                subTopic.getCreatedAt(),
                subTopic.getModifiedAt());
    }

    private static List<ResourceSubTopicView> mapResourceSubTopics(
            List<ResourceSubTopic> resourceSubTopics) {
        if (resourceSubTopics == null || resourceSubTopics.isEmpty()) {
            return List.of();
        }

        List<ResourceSubTopicView> resourceSubTopicViews =
                new ArrayList<>(resourceSubTopics.size());
        for (ResourceSubTopic resourceSubTopic : resourceSubTopics) {
            resourceSubTopicViews.add(toResourceSubTopicView(resourceSubTopic));
        }
        return resourceSubTopicViews;
    }

    private static ResourceSubTopicView toResourceSubTopicView(ResourceSubTopic resourceSubTopic) {
        return new ResourceSubTopicView(
                resourceSubTopic.getId(),
                resourceSubTopic.getName(),
                resourceSubTopic.getResourceType(),
                resourceSubTopic.getOrder(),
                resourceSubTopic.getLink());
    }
}
