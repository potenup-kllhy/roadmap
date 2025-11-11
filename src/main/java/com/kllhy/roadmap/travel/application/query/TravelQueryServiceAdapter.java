package com.kllhy.roadmap.travel.application.query;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.travel.application.view.TravelView;
import com.kllhy.roadmap.travel.domain.exception.TravelErrorCode;
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
                .findBatchById(travelId)
                .map(TravelView::of)
                .orElseThrow(() -> new DomainException(TravelErrorCode.TRAVEL_NOT_FOUND));
    }
}
