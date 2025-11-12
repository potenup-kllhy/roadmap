package com.kllhy.roadmap.roadmap.presentation;

import com.kllhy.roadmap.roadmap.application.command.RoadMapCommandService;
import com.kllhy.roadmap.roadmap.presentation.dto.RoadMapCreateRequest;
import com.kllhy.roadmap.roadmap.presentation.dto.mapper.CreateRoadMapCommandMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-v1/roadmap")
@RequiredArgsConstructor
public class RoadMapController {
    private final RoadMapCommandService roadMapCommandService;

    @PostMapping
    public ResponseEntity<?> createRoadMap(@Valid @RequestBody RoadMapCreateRequest request) {
        long id =
                roadMapCommandService.createRoadMap(
                        CreateRoadMapCommandMapper.mapCreateRoadMapCommand(request));
        return ResponseEntity.ok().body(id);
    }
}
