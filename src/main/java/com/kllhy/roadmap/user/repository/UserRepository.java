package com.kllhy.roadmap.user.repository;

import com.kllhy.roadmap.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
