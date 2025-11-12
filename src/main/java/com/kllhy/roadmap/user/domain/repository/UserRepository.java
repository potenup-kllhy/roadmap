package com.kllhy.roadmap.user.domain.repository;

import com.kllhy.roadmap.user.domain.model.User;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long id);

    Optional<User> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);

    boolean existsByEmail(String email);

    User save(User user);
}
