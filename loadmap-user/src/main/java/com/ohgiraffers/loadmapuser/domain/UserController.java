package com.ohgiraffers.loadmapuser.domain;

import com.ohgiraffers.loadmapuser.config.JwtUtil;
import com.ohgiraffers.loadmapuser.domain.command.dto.RegisterUserCommand;
import com.ohgiraffers.loadmapuser.domain.command.dto.UpdateUserCommand;
import com.ohgiraffers.loadmapuser.domain.command.service.UserCommandService;
import com.ohgiraffers.loadmapuser.domain.query.dto.UserQueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {
    private final UserCommandService userCommandService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserController(UserCommandService userCommandService, 
                         JwtUtil jwtUtil,
                         AuthenticationManager authenticationManager) {
        this.userCommandService = userCommandService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody RegisterUserCommand command) {
        log.info("Signup request received for loginId: {}", command.getLoginId());
        Long userId = userCommandService.registerUser(command);
        log.info("Signup successful for user id: {}", userId);
        return ResponseEntity.created(URI.create("/api/v1/users/" + userId)).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        log.info("Login attempt for loginId: {}", loginDTO.getLoginId());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getLoginId(), loginDTO.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user.getLoginId());
        
        log.info("Login successful for loginId: {}", loginDTO.getLoginId());

        return ResponseEntity.ok(new LoginResponseDTO(
                token, user.getLoginId(), user.getEmail(), user.getAccountStatus()
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<UserQueryResult> getMyUserInfo(@AuthenticationPrincipal User user) {
        if (user == null) {
            log.warn("Unauthorized access attempt to /me endpoint");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.debug("User info requested for user id: {}", user.getId());
        return ResponseEntity.ok(UserQueryResult.toQueryResult(user));
    }

    @PutMapping("/me")
    public ResponseEntity<UserQueryResult> updateMyUserInfo(@AuthenticationPrincipal User user,
                                                           @RequestBody UpdateUserCommand command) {
        if (user == null) {
            log.warn("Unauthorized update attempt to /me endpoint");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("Update request for user id: {}", user.getId());
        User updatedUser = userCommandService.updateUser(user.getId(), command);
        return ResponseEntity.ok(UserQueryResult.toQueryResult(updatedUser));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(@AuthenticationPrincipal User user) {
        if (user == null) {
            log.warn("Unauthorized delete attempt to /me endpoint");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("Account deletion request for user id: {}", user.getId());
        userCommandService.deleteUser(user.getId());
        return ResponseEntity.noContent().build();
    }
}
