package com.kllhy.roadmap.travel.domain.service;

import com.kllhy.roadmap.common.annotation.DomainService;
import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.travel.domain.exception.TravelErrorCode;
import com.kllhy.roadmap.travel.domain.model.Travel;
import com.kllhy.roadmap.travel.presentation.request.TravelUpdateRequest;
import com.kllhy.roadmap.user.domain.enums.AccountStatus;
import com.kllhy.roadmap.user.service.view.UserView;

@DomainService
public class TravelUpdateService {

    public Travel update(UserView user, Travel travel, TravelUpdateRequest request) {

        validUser(user, travel);

        var topicUpdates = request.progressTopicUpdates();
        var subUpdates = request.progressSubTopicUpdateRequests();

        // TODO ("일괄적용 생각해보기")
        if (topicUpdates != null) {
            for (var tu : topicUpdates) {
                travel.markTopic(tu.progressTopicId(), tu.status());
            }
        }

        if (subUpdates != null) {
            for (var su : subUpdates) {
                travel.markSubTopic(su.progressTopicId(), su.progressSubTopicId(), su.status());
            }
        }

        return travel;
    }

    private void validUser(UserView user, Travel travel) {
        if (!user.status().equals(AccountStatus.ACTIVE)) {
            throw new DomainException(TravelErrorCode.TRAVEL_USER_NOT_ACTIVE);
        }
        if (!travel.getUserId().equals(user.id())) {
            throw new DomainException(TravelErrorCode.TRAVEL_NOT_AUTHORITY);
        }
    }
}
