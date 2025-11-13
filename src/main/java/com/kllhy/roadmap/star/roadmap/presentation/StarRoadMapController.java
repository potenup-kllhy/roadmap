package com.kllhy.roadmap.star.roadmap.presentation;

import com.kllhy.roadmap.star.roadmap.application.command.StarRoadMapCommandService;
import com.kllhy.roadmap.star.roadmap.presentation.request.CreateStarRoadMapRequest;
import com.kllhy.roadmap.star.roadmap.presentation.request.DeleteStarRoadMapRequest;
import com.kllhy.roadmap.star.roadmap.presentation.request.UpdateStarRoadMapRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-v1/star-roadmaps")
@RequiredArgsConstructor
public class StarRoadMapController {

    private final StarRoadMapCommandService starRoadMapCommandService;

    @PostMapping
    public ResponseEntity<Void> createStarRoadMap(@RequestBody CreateStarRoadMapRequest request) {
        Long starRoadMapId =
                starRoadMapCommandService.create(
                        request.userId(), request.roadmapId(), request.value());
        return ResponseEntity.created(URI.create("/api-v1/star-roadmaps/" + starRoadMapId)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStarRoadMap(
            @PathVariable Long id, @RequestBody UpdateStarRoadMapRequest request) {
        starRoadMapCommandService.update(id, request.userId(), request.value());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteStarRoadMapByUserAndRoadmap(
            @RequestBody DeleteStarRoadMapRequest request) {
        starRoadMapCommandService.deleteByUserIdAndRoadmapId(
                request.userId(), request.roadmapId());
        return ResponseEntity.noContent().build();
    }
}
