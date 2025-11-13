package com.kllhy.roadmap.travel.application.service.command;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.roadmap.application.query.RoadMapQueryService;
import com.kllhy.roadmap.roadmap.application.query.dto.RoadMapView;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.travel.application.view.TravelView;
import com.kllhy.roadmap.travel.domain.exception.TravelErrorCode;
import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.repository.TravelRepository;
import com.kllhy.roadmap.travel.domain.service.TravelCreationService;
import com.kllhy.roadmap.travel.domain.service.TravelUpdateService;
import com.kllhy.roadmap.travel.presentation.request.TravelUpdateRequest;
import com.kllhy.roadmap.user.service.UserService;
import com.kllhy.roadmap.user.service.view.UserView;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TravelServiceAdapter implements TravelService {
    private final TravelRepository travelRepository;
    private final TravelCreationService travelCreationService;
    private final UserService userService;
    private final RoadMapQueryService roadMapQueryService;
    private final TravelUpdateService travelUpdateService;

    @Override
    public void create(Long userId, Long roadmapId) {
        UserView user = userService.getByUser(userId);
        RoadMapView roadmap = roadMapQueryService.findById(roadmapId);

        Travel travel = travelCreationService.create(user, roadmap);
        travelRepository.save(travel);
    }

    @Override
    public TravelView update(TravelUpdateRequest request) {
        Travel travel =
                travelRepository
                        .findBatchById(request.travelId())
                        .orElseThrow(() -> new DomainException(TravelErrorCode.TRAVEL_NOT_FOUND));
        UserView user = userService.getByUser(request.userId());

        Travel updated = travelUpdateService.update(user, travel, request);

        return TravelView.of(updated);
    }

    @Override
    public void syncArchivedOnRoadmap(Long roadmapId, ActiveStatus activeStatus) {
        travelRepository.findAllByRoadMapId(roadmapId).forEach(it -> it.activate(activeStatus));
    }

    @Override
    public void syncArchivedOnTopic(Long topicId, ActiveStatus activeStatus) {
        travelRepository
                .findAllByTopicId(topicId)
                .forEach(it -> it.activateTopic(topicId, activeStatus));
    }

    @Override
    public void syncCreateOnTopic(Long topicId) {
        List<Travel> travels = travelRepository.findAllByTopicId(topicId);
        travelCreationService.createTopic(travels, topicId);
    }

    // TODO("topicId를 과연 신뢰할수있는가?" 점점 무거워지는 aggregate Root)
    @Override
    public void syncArchivedOnSubTopic(Long topicId, Long subTopicId, ActiveStatus activeStatus) {
        Collection<Long> ids = travelRepository.findTravelIdsBySubTopicIds(List.of(subTopicId));
        if (ids.isEmpty()) return;
        List<Travel> travels = travelRepository.findByIdIn(ids);
        travels.forEach(t -> t.activateSubTopic(topicId, subTopicId, activeStatus));
    }

    @Override
    public void syncCreateOnSubTopic(Long topicId, Long subTopic) {
        var ids = travelRepository.findTravelIdsBySubTopicIds(List.of(subTopic));
        if (ids.isEmpty()) return;
        List<Travel> travels = travelRepository.findByIdIn(ids);
        travelCreationService.createSubTopic(travels, topicId, subTopic);
    }
}
