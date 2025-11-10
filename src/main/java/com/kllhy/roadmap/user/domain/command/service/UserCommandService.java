package com.kllhy.roadmap.user.domain.command.service;

import com.ohgiraffers.loadmapuser.domain.AccountStatus;
import com.ohgiraffers.loadmapuser.domain.User;
import com.ohgiraffers.loadmapuser.domain.UserRepository;
import com.ohgiraffers.loadmapuser.domain.command.dto.RegisterUserCommand;
import com.ohgiraffers.loadmapuser.domain.command.dto.UpdateUserCommand;
import com.ohgiraffers.loadmapuser.domain.exception.*;
import com.ohgiraffers.loadmapuser.domain.util.EmailValidator;
import com.ohgiraffers.loadmapuser.domain.util.PasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class UserCommandService {

    private static final Logger log = LoggerFactory.getLogger(UserCommandService.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCommandService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long registerUser(RegisterUserCommand command) {
        log.info("Registering new user with username: {}", command.getLoginId());
        
        if (userRepository.existsByLoginId(command.getLoginId())) {
            log.warn("Registration failed: Username already exists - {}", command.getLoginId());
            throw new DuplicateUsernameException("Username already exists.");
        }
        if (userRepository.existsByEmail(command.getEmail())) {
            log.warn("Registration failed: Email already exists - {}", command.getEmail());
            throw new DuplicateEmailException("Email already exists.");
        }

        EmailValidator.validate(command.getEmail());
        PasswordValidator.validate(command.getPassword());

        String encodedPassword = passwordEncoder.encode(command.getPassword());
        AccountStatus accountStatus = command.getAccountStatus() != null 
                ? command.getAccountStatus() 
                : AccountStatus.ACTIVE;

        User newUser = userRepository.save(new User(
                command.getLoginId(),
                command.getEmail(),
                encodedPassword,
                accountStatus
        ));
        
        log.info("User registered successfully with id: {}", newUser.getId());
        return newUser.getId();
    }

    public User updateUser(Long userId, UpdateUserCommand command) {
        log.info("Updating user with id: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Update failed: User not found with id: {}", userId);
                    return new UserNotFoundException("User not found with id: " + userId);
                });

        if (command.getEmail() != null) {
            EmailValidator.validate(command.getEmail());
            user.setEmail(command.getEmail());
        }
        if (command.getPassword() != null && !command.getPassword().isEmpty()) {
            PasswordValidator.validate(command.getPassword());
            user.setPassword(passwordEncoder.encode(command.getPassword()));
        }
        if (command.getAccountStatus() != null) {
            user.setAccountStatus(command.getAccountStatus());
        }
        user.setModifiedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        
        log.info("User updated successfully with id: {}", userId);
        return updatedUser;
    }

    public void updateAccountStatus(Long userId, AccountStatus status) {
        log.info("Updating account status for user id: {} to status: {}", userId, status);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Account status update failed: User not found with id: {}", userId);
                    return new UserNotFoundException("User not found with id: " + userId);
                });
        user.setAccountStatus(status);
        user.setModifiedAt(LocalDateTime.now());
        userRepository.save(user);
        
        log.info("Account status updated successfully for user id: {}", userId);
    }

    public void deleteUser(Long userId) {
        log.info("Deleting user with id: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Delete failed: User not found with id: {}", userId);
                    return new UserNotFoundException("User not found with id: " + userId);
                });
        
        if (user.getLeftAt() != null) {
            log.warn("Delete failed: User already deleted with id: {}", userId);
            throw new UserAlreadyDeletedException("User already deleted.");
        }
        
        user.setAccountStatus(AccountStatus.DISABLED);
        user.setLeftAt(LocalDateTime.now());
        user.setModifiedAt(LocalDateTime.now());
        userRepository.save(user);
        
        log.info("User deleted successfully with id: {}", userId);
    }
}

