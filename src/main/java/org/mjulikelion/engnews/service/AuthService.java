package org.mjulikelion.engnews.service;

import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.authentication.AccessTokenProvider;
import org.mjulikelion.engnews.authentication.PasswordHashEncryption;
import org.mjulikelion.engnews.dto.request.user.LoginDto;
import org.mjulikelion.engnews.dto.request.user.SignupDto;
import org.mjulikelion.engnews.dto.response.TokenResponseDto;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordHashEncryption passwordHashEncryption;
    private final UserRepository userRepository;
    private final AccessTokenProvider accessTokenProvider;
    private final UserService userService;

    public TokenResponseDto signup(SignupDto signupDto) {

        userService.validateIsDuplicatedName(signupDto.getName());

        userService.validateIsDuplicatedEmail(signupDto.getEmail());

        String plainPassword = signupDto.getPassword();
        String hashedPassword = passwordHashEncryption.encrypt(plainPassword);

        User newUser = User.builder()
                .email(signupDto.getEmail())
                .password(hashedPassword)
                .name(signupDto.getName())
                .build();
        userRepository.save(newUser);

        return createToken(newUser);
    }

    public TokenResponseDto login(LoginDto loginDto) {
        User user = userService.findExistingUserByEmail(loginDto.getEmail());

        userService.validateIsPasswordMatches(loginDto.getPassword(), user.getPassword());

        return createToken(user);
    }

    private TokenResponseDto createToken(User user) {
        String payload = String.valueOf(user.getId());
        String accessToken = accessTokenProvider.createToken(payload);

        return new TokenResponseDto(accessToken);
    }
}