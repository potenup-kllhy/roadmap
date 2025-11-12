package com.kllhy.roadmap.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.kllhy.roadmap.user.domain.User;
import com.kllhy.roadmap.user.domain.enums.AccountStatus;
import com.kllhy.roadmap.user.repository.UserRepository;
import com.kllhy.roadmap.user.service.UserService;
import com.kllhy.roadmap.user.service.view.UserView;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserService userService;
    @Mock private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    private User createMockUser(Long id, String email, AccountStatus status, Instant leftAt) {
        User mockUser = Mockito.mock(User.class);
        when(mockUser.getId()).thenReturn(id);
        when(mockUser.getEmail()).thenReturn(email);
        when(mockUser.getStatus()).thenReturn(status);
        when(mockUser.getLeftAt()).thenReturn(leftAt);
        return mockUser;
    }

    @Test
    void getByUser() {
        Long userId = 1L;
        String email = "roadmap@test.com";
        AccountStatus status = AccountStatus.ACTIVE;

        User mockUser = createMockUser(userId, email, status, null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        UserView view = userService.getByUser(userId);

        assertAll(
                () -> assertNotNull(view),
                () -> assertEquals(mockUser.getId(), view.id()),
                () -> assertEquals(mockUser.getEmail(), view.email()),
                () -> assertEquals(mockUser.getStatus(), view.status()));
    }
}
