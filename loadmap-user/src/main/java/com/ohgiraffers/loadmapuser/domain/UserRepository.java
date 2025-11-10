package com.ohgiraffers.loadmapuser.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByLoginId(String loginId);


    boolean existsByLoginId(String loginId);


    boolean existsByEmail(String email);
}