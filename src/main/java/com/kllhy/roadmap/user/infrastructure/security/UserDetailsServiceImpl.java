package com.kllhy.roadmap.user.infrastructure.security;

import com.kllhy.roadmap.user.domain.model.User;
import com.kllhy.roadmap.user.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user =
                userRepository
                        .findByLoginId(loginId)
                        .orElseThrow(() -> new UsernameNotFoundException(" " + loginId));
        return user;
    }
}
