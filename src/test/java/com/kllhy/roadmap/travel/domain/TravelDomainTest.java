package com.kllhy.roadmap.travel.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.kllhy.roadmap.travel.domain.model.Travel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TravelDomainTest {
    @Test
    @DisplayName("create success Travel")
    void create_success() {
        // given
        Long userId = 1L;
        Long roadmapId = 100L;

        // when
        Travel travel = Travel.create(userId, roadmapId);

        // then
        assertThat(travel.getUserId()).isEqualTo(userId);
        assertThat(travel.getRoadMapId()).isEqualTo(roadmapId);
    }
}
