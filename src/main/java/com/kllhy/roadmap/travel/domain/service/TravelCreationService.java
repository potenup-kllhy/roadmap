package com.kllhy.roadmap.travel.domain.service;

import com.kllhy.roadmap.common.annotation.DomainService;

@DomainService
public class TravelCreationService {

    // 여기서 검증 후 travel 반환 후 서비스에ㅓㅅ save
    // 해당 로드맵은 활성화 상태여야 한다
    // 또한 topic들도 활성화 상태인것만 가져와야함, -> 활성화시 도메인이벤트로 전달 후 생성 (이건 topic 들 update 부분에서 적용)

    //    public Travel create(User user, RoadMap roadMap) {
    //        //TODO domain exception 정의
    //        if (!roadMap.isPublish() || roadMap.isDeleted()) {
    //            throw new IllegalArgumentException();
    //        }
    //
    //        //  filter -> 활성화 된 것만 isDeleted와 publised가 false 인것만
    //        List<ProgressTopicCommand> topicCommands = roadMap.getTopics().stream()
    //                .map(t -> new ProgressTopicCommand(
    //                        t.getId(),
    //                        t.getSubTopics().stream()
    //                                .map(st -> new ProgressSubTopicCommand(st.getId()))
    //                                .toList()
    //                ))
    //                .toList();
    //
    //        Travel travel = Travel.create(user.getId(), roadMap.getId());
    //        travel.addTopics(topicCommands);
    //        return travel;
    //    }
}
