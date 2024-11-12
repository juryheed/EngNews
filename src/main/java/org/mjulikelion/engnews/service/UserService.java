package org.mjulikelion.engnews.service;

import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.authentication.PasswordHashEncryption;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHashEncryption passwordHashEncryption;

    public void validateIsPasswordMatches(String requestedPassword, String userPassword) {
        if (!passwordHashEncryption.matches(requestedPassword, userPassword)) {
            throw new UnauthorizedException(ErrorCode.INVALID_EMAIL_OR_PASSWORD);
        }
    }

    public void validateIsDuplicatedEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(ErrorCode.DUPLICATED_EMAIL);
        }
    }

    public void validateIsDuplicatedName(String name) {
        if (userRepository.existsByName(name)) {
            throw new ConflictException(ErrorCode.DUPLICATED_NAME);
        }
    }
    public User findExistingUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorCode.INVALID_EMAIL_OR_PASSWORD));
    }
}