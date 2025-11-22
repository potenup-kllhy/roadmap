package com.kllhy.roadmap.roadmap.presentation;

import com.kllhy.roadmap.common.response.ApiResponse;
import com.kllhy.roadmap.common.response.SuccessCode;
import com.kllhy.roadmap.roadmap.application.command.RoadMapCommandService;
import com.kllhy.roadmap.roadmap.application.query.RoadMapQueryService;
import com.kllhy.roadmap.roadmap.application.query.dto.RoadMapView;
import com.kllhy.roadmap.roadmap.presentation.dto.RoadMapCloneRequest;
import com.kllhy.roadmap.roadmap.presentation.dto.RoadMapCreateRequest;
import com.kllhy.roadmap.roadmap.presentation.dto.mapper.CreateRoadMapCommandMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-v1/roadmaps")
@RequiredArgsConstructor
public class RoadMapController {
    private final RoadMapCommandService roadMapCommandService;
    private final RoadMapQueryService roadMapQueryService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoadMapCreateResponse>> createRoadMap(
            @Valid @RequestBody RoadMapCreateRequest request) {
        long id =
                roadMapCommandService.createRoadMap(
                        CreateRoadMapCommandMapper.mapCreateRoadMapCommand(request));
        return ApiResponse.of(SuccessCode.SUCCESS, new RoadMapCreateResponse(id));
    }

    record RoadMapCreateResponse(long id) {}

    @PostMapping("/{id}/clone")
    public ResponseEntity<?> cloneRoadMap(
            @PathVariable("id") long id, @RequestBody @Valid RoadMapCloneRequest request) {
        long cloneRoadMapId =
                roadMapCommandService.cloneRoadMap(id, request.userId(), request.categoryId());
        return ApiResponse.of(SuccessCode.SUCCESS, new RoadMapCreateResponse(cloneRoadMapId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoadMapById(@PathVariable("id") long id) {
        RoadMapView view = roadMapQueryService.findByIdWithAssociations(id);
        return ApiResponse.of(SuccessCode.SUCCESS, view);
    }
}
