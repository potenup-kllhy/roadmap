package com.kllhy.roadmap.star.roadmap.application.command;


public interface StarRoadMapCommandService {
    Long create(Long userId, Long roadmapId, int value);

    void update(Long starRoadmapId, Long userId, int value);

    void deleteByUserIdAndRoadmapId(Long userId, Long roadmapId);

    void deleteAllStarByUserId(Long userId);

    void deleteAllStarByRoadMapId(Long roadmapId);
}
