package com.kllhy.roadmap.star.roadmap.application.command;

public interface StarRoadMapCommandService {
    Long create(Long userId, Long roadmapId, int value);

    void update(Long userId, Long roadmapId, int value);

    void deleteById(Long starId);

    void deleteAllStarByUserId(Long userId);

    void deleteAllStarByRoadMapId(Long roadmapId);
}
