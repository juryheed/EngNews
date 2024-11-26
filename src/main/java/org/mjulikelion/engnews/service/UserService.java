package org.mjulikelion.engnews.service;

import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.authentication.PasswordHashEncryption;
import org.mjulikelion.engnews.dto.request.user.DeleteUserDto;
import org.mjulikelion.engnews.dto.response.user.MypageDto;
import org.mjulikelion.engnews.entity.Category;
import org.mjulikelion.engnews.entity.Keyword;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.exception.ConflictException;
import org.mjulikelion.engnews.exception.ErrorCode;
import org.mjulikelion.engnews.exception.NotFoundException;
import org.mjulikelion.engnews.exception.UnauthorizedException;
import org.mjulikelion.engnews.repository.CategoryRepository;
import org.mjulikelion.engnews.repository.KeywordRepository;
import org.mjulikelion.engnews.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHashEncryption passwordHashEncryption;
    private final CategoryRepository categoryRepository;
    private final KeywordRepository keywordRepository;

    public MypageDto getMypage(User user) {

        // 유저와 연결된 카테고리 리스트 가져오기
        List<Category> categories = categoryRepository.findAllByUser(user);

        // 카테고리 리스트와 연결된 키워드 가져오기
        List<Keyword> keywords = keywordRepository.findAllByCategoryIn(categories);

        return MypageDto.from(user, keywords, categories);
    }

    public void deleteUser(User user, DeleteUserDto deleteUserDto) {
        validateIsPasswordMatches(deleteUserDto.getPassword(), user.getPassword());
        userRepository.delete(user);
    }

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