package com.kllhy.roadmap.travel.presentation;

import com.kllhy.roadmap.common.response.ApiResponse;
import com.kllhy.roadmap.common.response.SuccessCode;
import com.kllhy.roadmap.travel.application.service.command.TravelService;
import com.kllhy.roadmap.travel.application.service.query.TravelQueryService;
import com.kllhy.roadmap.travel.application.view.TravelView;
import com.kllhy.roadmap.travel.presentation.request.TravelCreateRequest;
import com.kllhy.roadmap.travel.presentation.request.TravelUpdateRequest;
import com.kllhy.roadmap.travel.presentation.response.TravelResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-v1/travels")
@RequiredArgsConstructor
public class TravelController {
    private final TravelService travelService;
    private final TravelQueryService travelQueryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Valid @RequestBody TravelCreateRequest request) {
        Long id = travelService.create(request.userId(), request.roadmapId());
        return ApiResponse.of(SuccessCode.CREATED, id);
    }

    @GetMapping("{/id}")
    public ResponseEntity<ApiResponse<TravelResponse>> get(@PathVariable Long id) {
        TravelView view = travelQueryService.getById(id);
        return ApiResponse.of(SuccessCode.SUCCESS, TravelResponse.of(view));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<TravelResponse>> update(
            @Valid @RequestBody TravelUpdateRequest request) {
        TravelView view = travelService.update(request);
        return ApiResponse.of(SuccessCode.SUCCESS, TravelResponse.of(view));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TravelResponse>>> getByUser(@PathVariable Long userId) {
        var views = travelQueryService.getByUser(userId).stream().map(TravelResponse::of).toList();

        return ApiResponse.of(SuccessCode.SUCCESS, views);
    }
}
