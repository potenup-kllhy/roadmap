package com.kllhy.roadmap.travel.application.query;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.travel.application.query.dto.TravelView;
import com.kllhy.roadmap.travel.domain.exception.TravelErrorCode;
import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelQueryServiceAdapter implements TravelQueryService {
    private final TravelRepository travelRepository;

    @Override
    public TravelView getById(Long travelId) {
        return travelRepository
                .findById(travelId)
                .map(this::toView)
                .orElseThrow(() -> new DomainException(TravelErrorCode.TRAVEL_NOT_FOUND));
    }

    private TravelView toView(Travel travel) {
        return new TravelView(travel.getId(), travel.getUserId(), travel.getRoadMapId());
    }
}
