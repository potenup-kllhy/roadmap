package com.kllhy.roadmap.travel.domain.service;

import com.kllhy.roadmap.common.annotation.DomainService;
import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.roadmap.application.query.dto.RoadMapView;
import com.kllhy.roadmap.travel.domain.exception.TravelErrorCode;
import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.model.command.ProgressSubTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.ProgressTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.TravelCommand;
import com.kllhy.roadmap.user.domain.enums.AccountStatus;
import com.kllhy.roadmap.user.service.view.UserView;
import java.util.List;

@DomainService
public class TravelCreationService {

    public Travel create(UserView user, RoadMapView roadMap) {
        if (!user.status().equals(AccountStatus.ACTIVE)) {
            throw new DomainException(TravelErrorCode.TRAVEL_USER_NOT_ACTIVE);
        }

        if (!roadMap.isDraft() || roadMap.isDeleted()) {
            throw new DomainException(TravelErrorCode.TRAVEL_ROADMAP_INVALID);
        }

        List<ProgressTopicCommand> topicCommands =
                roadMap.topics().stream()
                        .map(
                                t ->
                                        new ProgressTopicCommand(
                                                t.id(),
                                                t.subTopics().stream()
                                                        .map(
                                                                st ->
                                                                        new ProgressSubTopicCommand(
                                                                                st.id()))
                                                        .toList()))
                        .toList();

        TravelCommand travelCommand = new TravelCommand(user.id(), roadMap.id(), topicCommands);

        return Travel.create(travelCommand);
    }
}
