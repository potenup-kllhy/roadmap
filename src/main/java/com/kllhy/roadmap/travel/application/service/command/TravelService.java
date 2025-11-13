package com.kllhy.roadmap.travel.application.service.command;

import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.travel.application.view.TravelView;
import com.kllhy.roadmap.travel.presentation.request.TravelUpdateRequest;

public interface TravelService {
    public void create(Long userId, Long roadmapId);

    public TravelView update(TravelUpdateRequest request);

    public void syncArchivedOnRoadmap(Long roadmapId, ActiveStatus activeStatus);

    public void syncArchivedOnTopic(Long topicId, ActiveStatus activeStatus);

    public void syncCreateOnTopic(Long topicId);

    public void syncArchivedOnSubTopic(Long topicId, Long subTopicId, ActiveStatus activeStatus);

    public void syncCreateOnSubTopic(Long topicId, Long subTopic);
}
