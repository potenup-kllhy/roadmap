package com.kllhy.roadmap.travel.application.event;

import com.kllhy.roadmap.roadmap.domain.event.RoadMapEventOccurred;
import com.kllhy.roadmap.roadmap.domain.event.SubTopicEventOccurred;
import com.kllhy.roadmap.roadmap.domain.event.TopicEventOccurred;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.roadmap.domain.event.enums.EventType;
import com.kllhy.roadmap.travel.application.service.command.TravelService;
import jakarta.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TravelEventHandler {
    private final TravelService travelService;
    private final Map<EventType, Consumer<TopicEventOccurred>> topicHandlers =
            new EnumMap<>(EventType.class);
    private final Map<EventType, Consumer<SubTopicEventOccurred>> subTopicHandlers =
            new EnumMap<>(EventType.class);

    @PostConstruct
    void init() {
        topicHandlers.put(EventType.CREATED, e -> travelService.syncCreateOnTopic(e.topicId()));

        topicHandlers.put(
                EventType.UPDATED,
                e -> travelService.syncArchivedOnTopic(e.topicId(), e.activeStatus()));

        topicHandlers.put(
                EventType.DELETED,
                e -> travelService.syncArchivedOnTopic(e.topicId(), ActiveStatus.INACTIVE));

        // subTopic
        subTopicHandlers.put(
                EventType.UPDATED,
                e ->
                        travelService.syncArchivedOnSubTopic(
                                e.topicId(), e.subTopicId(), e.activeStatus()));

        subTopicHandlers.put(
                EventType.DELETED,
                e ->
                        travelService.syncArchivedOnSubTopic(
                                e.topicId(), e.subTopicId(), ActiveStatus.INACTIVE));

        subTopicHandlers.put(
                EventType.CREATED,
                e -> travelService.syncCreateOnSubTopic(e.topicId(), e.subTopicId()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void roadmapHandler(RoadMapEventOccurred event) {
        travelService.syncArchivedOnRoadmap(event.roadMapId(), event.activeStatus());
    }

    // TODO ("추후 전략패턴사용")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void topicHandler(TopicEventOccurred event) {
        var h = topicHandlers.get(event.eventType());
        if (h == null) return;
        h.accept(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void subTopicHandler(SubTopicEventOccurred event) {
        var h = subTopicHandlers.get(event.eventType());
        if (h == null) return;
        h.accept(event);
    }
}
