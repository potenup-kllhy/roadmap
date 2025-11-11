package com.kllhy.roadmap.travel.application.command;

import com.kllhy.roadmap.travel.domain.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TravelServiceAdapter implements TravelService {
    private final TravelRepository travelRepository;

    @Override
    public void create(Long userId, Long roadmapId) {
        // user와 roadmap 찾아와서
        // domain service에서 정책 검증 후
        // entity 에서 불변 조건 으로 생성?

    }
}
