package com.kllhy.roadmap.roadmap.application.command.dto.mapper;

import com.kllhy.roadmap.roadmap.application.command.dto.*;
import com.kllhy.roadmap.roadmap.domain.model.creation_spec.*;
import java.util.ArrayList;
import java.util.List;

public class CreationRoadMapMapper {
    public static CreationRoadMap toCreationRoadMap(CreateRoadMapCommand command) {
        return new CreationRoadMap(
                command.title(),
                command.description(),
                command.isDraft(),
                command.categoryId(),
                command.userId(),
                mapTopics(command.topics()));
    }

    private static List<CreationTopic> mapTopics(List<CreateTopicCommand> commands) {
        if (commands == null || commands.isEmpty()) {
            return List.of();
        }

        List<CreationTopic> topics = new ArrayList<>();
        for (CreateTopicCommand command : commands) {
            topics.add(toTopic(command));
        }
        return topics;
    }

    private static CreationTopic toTopic(CreateTopicCommand command) {
        return new CreationTopic(
                command.title(),
                command.content(),
                command.importanceLevel(),
                command.order(),
                command.isDraft(),
                mapResourceTopics(command.resourceTopics()),
                mapSubTopics(command.subTopics()));
    }

    private static List<CreationResourceTopic> mapResourceTopics(
            List<CreateResourceTopicCommand> commands) {
        if (commands == null || commands.isEmpty()) {
            return List.of();
        }

        List<CreationResourceTopic> resourceTopics = new ArrayList<>();
        for (CreateResourceTopicCommand command : commands) {
            resourceTopics.add(toResourceTopic(command));
        }
        return resourceTopics;
    }

    private static CreationResourceTopic toResourceTopic(CreateResourceTopicCommand command) {
        return new CreationResourceTopic(
                command.name(), command.resourceType(), command.order(), command.link());
    }

    private static List<CreationSubTopic> mapSubTopics(List<CreateSubTopicCommand> commands) {
        if (commands == null || commands.isEmpty()) {
            return List.of();
        }

        List<CreationSubTopic> subTopics = new ArrayList<>();
        for (CreateSubTopicCommand command : commands) {
            subTopics.add(toSubTopic(command));
        }
        return subTopics;
    }

    private static CreationSubTopic toSubTopic(CreateSubTopicCommand command) {
        return new CreationSubTopic(
                command.title(),
                command.content(),
                command.importanceLevel(),
                command.isDraft(),
                mapResourceSubTopics(command.resourceSubTopics()));
    }

    private static List<CreationResourceSubTopic> mapResourceSubTopics(
            List<CreateResourceSubTopicCommand> commands) {
        if (commands == null || commands.isEmpty()) {
            return List.of();
        }

        List<CreationResourceSubTopic> resourceSubTopics = new ArrayList<>();
        for (CreateResourceSubTopicCommand command : commands) {
            resourceSubTopics.add(toResourceSubTopic(command));
        }
        return resourceSubTopics;
    }

    private static CreationResourceSubTopic toResourceSubTopic(
            CreateResourceSubTopicCommand command) {
        return new CreationResourceSubTopic(
                command.name(), command.order(), command.resourceType(), command.link());
    }
}
