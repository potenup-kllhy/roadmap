package com.kllhy.roadmap.travel.application.event;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import com.kllhy.roadmap.roadmap.domain.event.RoadMapEventOccurred;
import com.kllhy.roadmap.roadmap.domain.event.SubTopicEventOccurred;
import com.kllhy.roadmap.roadmap.domain.event.TopicEventOccurred;
import com.kllhy.roadmap.roadmap.domain.event.enums.ActiveStatus;
import com.kllhy.roadmap.roadmap.domain.event.enums.EventType;
import com.kllhy.roadmap.travel.application.service.command.TravelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
public class TravelEventHandlerTest {

    @MockitoBean private TravelService travelService;

    @Autowired private ApplicationEventPublisher publisher;

    @Autowired private TransactionTemplate txTemplate;

    @Test
    void roadmap_event_routes_to_service_after_commit() {
        var e = new RoadMapEventOccurred(100L, EventType.UPDATED, ActiveStatus.INACTIVE);

        txTemplate.executeWithoutResult(tx -> publisher.publishEvent(e));

        verify(travelService, timeout(1000)).syncArchivedOnRoadmap(100L, ActiveStatus.INACTIVE);
    }

    @Test
    void topic_created_routes() {
        var e = new TopicEventOccurred(100L, 123L, EventType.UPDATED, ActiveStatus.ACTIVE);

        txTemplate.executeWithoutResult(tx -> publisher.publishEvent(e));

        verify(travelService, timeout(1000)).syncArchivedOnTopic(123L, ActiveStatus.ACTIVE);
    }

    @Test
    void topic_updated_routes() {
        var e = new TopicEventOccurred(100L, 123L, EventType.CREATED, null);

        txTemplate.executeWithoutResult(tx -> publisher.publishEvent(e));

        verify(travelService, timeout(1000)).syncCreateOnTopic(123L);
    }

    @Test
    void topic_deleted_routes_as_inactive() {
        var e = new TopicEventOccurred(100L, 123L, EventType.DELETED, null);

        txTemplate.executeWithoutResult(tx -> publisher.publishEvent(e));

        verify(travelService, timeout(1000)).syncArchivedOnTopic(123L, ActiveStatus.INACTIVE);
    }

    @Test
    void subtopic_updated_routes() {
        var e =
                new SubTopicEventOccurred(
                        100L, 10L, 101L, EventType.UPDATED, ActiveStatus.INACTIVE);

        txTemplate.executeWithoutResult(tx -> publisher.publishEvent(e));

        verify(travelService, timeout(1000))
                .syncArchivedOnSubTopic(10L, 101L, ActiveStatus.INACTIVE);
    }

    @Test
    void subtopic_deleted_routes_as_inactive() {
        var e = new SubTopicEventOccurred(100L, 10L, 101L, EventType.DELETED, null);

        txTemplate.executeWithoutResult(tx -> publisher.publishEvent(e));

        verify(travelService, timeout(1000))
                .syncArchivedOnSubTopic(10L, 101L, ActiveStatus.INACTIVE);
    }

    @Test
    void subtopic_created_routes_current_logic() {
        var e = new SubTopicEventOccurred(100L, 10L, 101L, EventType.CREATED, null);

        txTemplate.executeWithoutResult(tx -> publisher.publishEvent(e));

        verify(travelService, timeout(1000)).syncCreateOnSubTopic(10L, 101L);
    }
}
