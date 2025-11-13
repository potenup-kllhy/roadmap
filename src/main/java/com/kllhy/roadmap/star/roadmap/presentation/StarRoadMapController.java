package com.kllhy.roadmap.star.roadmap.presentation;

import com.kllhy.roadmap.star.roadmap.application.command.StarRoadMapCommandService;
import com.kllhy.roadmap.star.roadmap.domain.model.command.CreateStarRoadMapCommand;
import com.kllhy.roadmap.star.roadmap.domain.model.command.UpdateStarRoadMapCommand;
import com.kllhy.roadmap.star.roadmap.presentation.request.CreateStarRoadMapRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-v1/star-roadmaps")
@RequiredArgsConstructor
public class StarRoadMapController {

    private final StarRoadMapCommandService starRoadMapCommandService;

    @PostMapping
    public ResponseEntity<Void> createStarRoadMap(@RequestBody CreateStarRoadMapRequest request) {
        CreateStarRoadMapCommand command =
                new CreateStarRoadMapCommand(
                        request.userId(), request.roadmapId(), request.value());
        Long starRoadMapId = starRoadMapCommandService.create(command);
        return ResponseEntity.created(URI.create("/api-v1/star-roadmaps/" + starRoadMapId)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStarRoadMap(
            @PathVariable Long id, @RequestBody UpdateStarRoadMapRequest request) {
        UpdateStarRoadMapCommand command =
                new UpdateStarRoadMapCommand(request.userId(), id, request.value());
        starRoadMapCommandService.update(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStarRoadMap(@PathVariable Long id) {
        starRoadMapCommandService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteStarRoadMapByUserAndRoadmap(
            @RequestParam Long userId, @RequestParam Long roadmapId) {
        starRoadMapCommandService.deleteByUserIdAndRoadmapId(userId, roadmapId);
        return ResponseEntity.noContent().build();
    }
}
