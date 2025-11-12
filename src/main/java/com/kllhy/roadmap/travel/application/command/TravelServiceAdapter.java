package com.kllhy.roadmap.travel.application.command;

import com.kllhy.roadmap.roadmap.application.query.RoadMapQueryService;
import com.kllhy.roadmap.roadmap.application.query.dto.RoadMapView;
import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.repository.TravelRepository;
import com.kllhy.roadmap.travel.domain.service.TravelCreationService;
import com.kllhy.roadmap.user.service.UserService;
import com.kllhy.roadmap.user.service.view.UserView;
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

    @Override
    public void create(Long userId, Long roadmapId) {
        UserView user = userService.getByUser(userId);
        RoadMapView roadmap = roadMapQueryService.findById(roadmapId);

        Travel travel = travelCreationService.create(user, roadmap);

    }
}
