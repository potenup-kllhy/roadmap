package com.kllhy.roadmap.user;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import com.kllhy.roadmap.user.domain.User;
import com.kllhy.roadmap.user.domain.enums.AccountStatus;
import com.kllhy.roadmap.user.event.handler.UserDomainEventHandler;
import com.kllhy.roadmap.user.repository.UserRepository;
import com.kllhy.roadmap.user.service.UserService;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.event.RecordApplicationEvents;

@SpringBootTest
@RecordApplicationEvents
public class UserDomainEventTest {

    @Autowired private UserService userService;

    @Autowired private UserRepository userRepository;

    @MockitoSpyBean UserDomainEventHandler handler;

    private Long savedUserId;

    @BeforeEach
    void setUp() {
        String email = "roadmap@test.com";
        String password = "testPassword";

        User user = User.create(email, password);

        User savedUser = userRepository.save(user);
        savedUserId = savedUser.getId();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void handler_is_invoked_after_commit() {

        userService.withdrawal(savedUserId, AccountStatus.DISABLED);

        verify(handler, timeout(1500))
                .on(
                        argThat(
                                e ->
                                        e.userId().equals(savedUserId)
                                                && "DISABLED".equals(e.newStatus())));
    }

    @Test
    void logs_hello_in_handler() {
        var logCaptor = LogCaptor.forClass(UserDomainEventHandler.class);

        userService.withdrawal(savedUserId, AccountStatus.DISABLED);

        await().untilAsserted(
                        () ->
                                assertThat(logCaptor.getInfoLogs())
                                        .anyMatch(msg -> msg.contains("hello!!!!")));
    }
}
