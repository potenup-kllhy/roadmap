package com.kllhy.roadmap.user.domain.query.service;

import com.ohgiraffers.loadmapuser.domain.User;
import com.ohgiraffers.loadmapuser.domain.UserRepository;
import com.ohgiraffers.loadmapuser.domain.query.dto.UserQueryResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserQueryService {
    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserQueryResult> findUserQueryResultById(Long id) {
        return userRepository.findById(id)
                .map(UserQueryResult::toQueryResult);
    }
}

