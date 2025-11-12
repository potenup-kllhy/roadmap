package com.kllhy.roadmap.travel.application.command;

import com.kllhy.roadmap.travel.domain.repository.TravelRepository;
import com.kllhy.roadmap.travel.domain.service.TravelCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TravelServiceAdapter implements TravelService {
    private final TravelRepository travelRepository;
    private final TravelCreationService travelCreationService;

    @Override
    public void create(Long userId, Long roadmapId) {
        Long tempUserId = 1L;
        Long tempRoadmapId = 1L;

        //        travelCreationService.

    }
}
