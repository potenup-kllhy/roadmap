package com.kllhy.roadmap.travel.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.model.command.ProgressSubTopicCommand;
import com.kllhy.roadmap.travel.domain.model.command.ProgressTopicCommand;
import com.kllhy.roadmap.travel.domain.model.read.TravelSnapshot;
import com.kllhy.roadmap.travel.domain.repository.TravelRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TravelSnapshotTest {

    @Autowired private TravelRepository travelRepository;

    @Autowired private EntityManager em;

    @Test
    @DisplayName("create snapshot test")
    public void snapshot_creation_success() {
        Long userId = 1L;
        Long roadMapId = 100L;
        Travel travel = Travel.create(userId, roadMapId);

        List<ProgressTopicCommand> topicCommands =
                IntStream.rangeClosed(1, 10)
                        .mapToObj(
                                i -> {
                                    Long topicId = (long) i * 10;
                                    List<ProgressSubTopicCommand> subTopicCommands =
                                            IntStream.rangeClosed(1, 5)
                                                    .mapToObj(
                                                            j ->
                                                                    new ProgressSubTopicCommand(
                                                                            topicId + j))
                                                    .toList();
                                    return new ProgressTopicCommand(topicId, subTopicCommands);
                                })
                        .toList();

        travel.addTopics(topicCommands);
        travelRepository.save(travel);

        em.flush();
        em.clear();

        Travel findTravel =
                travelRepository.findBatchByRoadmapIdAndUserId(roadMapId, userId).orElseThrow();
        TravelSnapshot snapshot = findTravel.toSnapshot();

        System.out.println("=== Snapshot Verification Start ===");
        System.out.println("Travel ID: " + snapshot.travelId());
        System.out.println("Status: " + snapshot.status());
        System.out.println("Total Topics: " + snapshot.topics().size());

        assertThat(snapshot).isNotNull();
        assertThat(snapshot.travelId()).isEqualTo(findTravel.getId());
        assertThat(snapshot.userId()).isEqualTo(userId);
        assertThat(snapshot.roadMapId()).isEqualTo(roadMapId);
        assertThat(snapshot.topics()).hasSize(10);

        TravelSnapshot.ProgressTopicSnapshot firstTopicSnap = snapshot.topics().get(0);
        System.out.println("\n--- First Topic Snapshot ---");
        System.out.println("Topic ID: " + firstTopicSnap.topicId());
        System.out.println("SubTopics Count: " + firstTopicSnap.subTopics().size());
        firstTopicSnap
                .subTopics()
                .forEach(
                        sub ->
                                System.out.println(
                                        "  - SubTopic ID: "
                                                + sub.subTopicId()
                                                + ", Status: "
                                                + sub.status()));

        assertThat(firstTopicSnap.topicId()).isEqualTo(10L);
        assertThat(firstTopicSnap.subTopics()).hasSize(5);
        assertThat(firstTopicSnap.subTopics().get(0).subTopicId()).isEqualTo(11L);

        System.out.println("=== Snapshot Verification End ===");
    }
}
