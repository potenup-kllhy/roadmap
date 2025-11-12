package com.kllhy.roadmap.user.service;

import com.kllhy.roadmap.common.exception.DomainException;
import com.kllhy.roadmap.user.domain.User;
import com.kllhy.roadmap.user.domain.enums.AccountStatus;
import com.kllhy.roadmap.user.exception.UserErrorCode;
import com.kllhy.roadmap.user.repository.UserRepository;
import com.kllhy.roadmap.user.service.view.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserView getByUser(Long userId) {
        return userRepository
                .findById(userId)
                .map(UserView::of)
                .orElseThrow(() -> new DomainException(UserErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void withdrawal(Long userId, AccountStatus status) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new DomainException(UserErrorCode.USER_NOT_FOUND));

        user.updateStatus(status);
        userRepository.save(user);
    }
}
