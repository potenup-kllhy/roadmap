package com.kllhy.roadmap.user.domain.query.service;

<<<<<<< HEAD
import com.ohgiraffers.loadmapuser.domain.User;
import com.ohgiraffers.loadmapuser.domain.UserRepository;
import com.ohgiraffers.loadmapuser.domain.query.dto.UserQueryResult;
import java.util.Optional;
=======
import com.kllhy.roadmap.user.domain.User;
import com.kllhy.roadmap.user.domain.UserRepository;
import com.kllhy.roadmap.user.domain.query.dto.UserQueryResult;
>>>>>>> 51513f5 (fix(user):typo)
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return userRepository.findById(id).map(UserQueryResult::toQueryResult);
    }
}
