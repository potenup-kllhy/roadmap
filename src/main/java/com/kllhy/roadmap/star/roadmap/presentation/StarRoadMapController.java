package com.kllhy.roadmap.star.roadmap.presentation;

import com.kllhy.roadmap.star.roadmap.application.command.StarRoadMapCommandService;
import com.kllhy.roadmap.star.roadmap.application.query.StarRoadMapQueryService;
import com.kllhy.roadmap.star.roadmap.application.query.dto.StarRoadMapView;
import com.kllhy.roadmap.star.roadmap.presentation.exception.ErrorResponse;
import com.kllhy.roadmap.star.roadmap.presentation.request.CreateStarRoadMapRequest;
import com.kllhy.roadmap.star.roadmap.presentation.request.DeleteStarRoadMapRequest;
import com.kllhy.roadmap.star.roadmap.presentation.request.UpdateStarRoadMapRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-v1/star-roadmaps")
@RequiredArgsConstructor
public class StarRoadMapController {

    private final StarRoadMapCommandService starRoadMapCommandService;
    private final StarRoadMapQueryService starRoadMapQueryService;

    @PostMapping
    public ResponseEntity<Void> createStarRoadMap(@RequestBody CreateStarRoadMapRequest request) {
        Long starRoadMapId =
                starRoadMapCommandService.create(
                        request.userId(), request.roadmapId(), request.value());
        return ResponseEntity.created(URI.create("/api-v1/star-roadmaps/" + starRoadMapId)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StarRoadMapView> getStarRoadMapById(@PathVariable Long id) {
        var starRoadMapView = starRoadMapQueryService.getById(id);
        return ResponseEntity.ok(starRoadMapView);
    }

    @GetMapping
    public ResponseEntity<?> getStarRoadMaps(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long roadmapId) {
        if (userId != null) {
            var starRoadMapViews = starRoadMapQueryService.getAllStarByUserId(userId);
            return ResponseEntity.ok(starRoadMapViews);
        }
        if (roadmapId != null) {
            var starRoadMapViews = starRoadMapQueryService.getAllStarByRoadMapId(roadmapId);
            return ResponseEntity.ok(starRoadMapViews);
        }

        ErrorResponse errorResponse =
                new ErrorResponse(
                        HttpStatus.BAD_REQUEST, "Either userId or roadmapId must be provided.");
        return ResponseEntity.badRequest().body(errorResponse);
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
        starRoadMapCommandService.deleteByUserIdAndRoadmapId(request.userId(), request.roadmapId());
        return ResponseEntity.noContent().build();
    }
}
