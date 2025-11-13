package com.kllhy.roadmap.travel.application.service.query;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.travel.application.view.TravelView;
import com.kllhy.roadmap.travel.domain.exception.TravelErrorCode;
import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.domain.repository.TravelRepository;
import com.kllhy.roadmap.user.domain.enums.AccountStatus;
import com.kllhy.roadmap.user.service.UserService;
import com.kllhy.roadmap.user.service.view.UserView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelQueryServiceAdapter implements TravelQueryService {
    private final TravelRepository travelRepository;
    private final UserService userService;

    @Override
    public TravelView getById(Long travelId) {
        Travel travel =
                travelRepository
                        .findBatchById(travelId)
                        .orElseThrow(() -> new DomainException(TravelErrorCode.TRAVEL_NOT_FOUND));

        if (travel.isArchived()) {
            throw new DomainException(TravelErrorCode.TRAVEL_NOT_FOUND);
        }

        return TravelView.of(travel);
    }

    @Override
    public List<TravelView> getByUser(Long userId) {

        UserView user = userService.getByUser(userId);
        if (!user.status().equals(AccountStatus.ACTIVE)) {
            throw new DomainException(TravelErrorCode.TRAVEL_NOT_AUTHORITY);
        }

        return travelRepository.findBatchByUserId(userId, Pageable.unpaged()).stream()
                .filter(it -> !it.isArchived())
                .map(TravelView::of)
                .toList();
    }
}
