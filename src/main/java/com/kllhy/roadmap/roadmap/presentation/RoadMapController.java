package com.kllhy.roadmap.roadmap.presentation;

import com.kllhy.roadmap.common.response.ApiResponse;
import com.kllhy.roadmap.common.response.SuccessCode;
import com.kllhy.roadmap.roadmap.application.command.RoadMapCommandService;
import com.kllhy.roadmap.roadmap.presentation.dto.RoadMapCreateRequest;
import com.kllhy.roadmap.roadmap.presentation.dto.mapper.CreateRoadMapCommandMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-v1/roadmap")
@RequiredArgsConstructor
public class RoadMapController {
    private final RoadMapCommandService roadMapCommandService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoadMapCreateResponse>> createRoadMap(
            @Valid @RequestBody RoadMapCreateRequest request) {
        long id =
                roadMapCommandService.createRoadMap(
                        CreateRoadMapCommandMapper.mapCreateRoadMapCommand(request));
        return ApiResponse.of(SuccessCode.SUCCESS, new RoadMapCreateResponse(id));
    }

    @PostMapping("/{id}/clone")
    public ResponseEntity<?> cloneRoadMap(@PathVariable("id") long id) {
        long cloneRoadMapId = roadMapCommandService.cloneRoadMap(id);
        return ApiResponse.of(SuccessCode.SUCCESS, new RoadMapCreateResponse(cloneRoadMapId));
    }

    record RoadMapCreateResponse(long id) {}
}
