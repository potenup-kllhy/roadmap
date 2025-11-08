package com.kllhy.roadmap.travel.domain.repository;

import com.kllhy.roadmap.travel.domain.model.Travel;

import java.util.Optional;

public interface TravelRepository {

    Optional<Travel> findById(Long id);

}