package org.mjulikelion.engnews.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.dto.request.keyword.KeywordDto;
import org.mjulikelion.engnews.dto.response.keyword.CategoryKeywordListResponseDto;
import org.mjulikelion.engnews.dto.response.keyword.KeywordOptionListResponseDto;
import org.mjulikelion.engnews.entity.*;
import org.mjulikelion.engnews.exception.ErrorCode;
import org.mjulikelion.engnews.exception.NotFoundException;
import org.mjulikelion.engnews.repository.CategoryOptionsRepository;
import org.mjulikelion.engnews.repository.CategoryRepository;
import org.mjulikelion.engnews.repository.KeywordOptionsRepository;
import org.mjulikelion.engnews.repository.KeywordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryOptionsRepository categoryOptionsRepository;
    private final KeywordOptionsRepository keywordOptionsRepository;

    // 키워드 목록 조회
    public KeywordOptionListResponseDto getAllKeywords(UUID categoryId) {
        CategoryOptions categoryOptions = categoryOptionsRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
        List<KeywordOptions> keywordOptions = keywordOptionsRepository.findAllByCategoryOptions(categoryOptions);

        return KeywordOptionListResponseDto.from(keywordOptions);
    }

    // 유저 카테고리에 해당하는 키워드 저장
    public void saveKeyword(User user, KeywordDto keywordDto) {
        KeywordOptions keywordOptions = keywordOptionsRepository.findById(keywordDto.getKeywordId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.KEYWORD_NOT_FOUMD));
        Category category = categoryRepository.findById(keywordDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        Keyword keyword = Keyword.builder()
                .keywordOptions(keywordOptions)
                .category(category)
                .user(user)
                .build();
        keywordRepository.save(keyword);
    }

    // 카테고리에 해당하는 키워드 목록 조회
    public CategoryKeywordListResponseDto getKeyword(User user, UUID categoryId){
        Category category = categoryRepository.findByUserAndId(user,categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
        List<Keyword> keywords = keywordRepository.findAllByCategory(category);

        return CategoryKeywordListResponseDto.from(keywords);
    }

    // 키워드 삭제
    public void deleteKeyword(User user, UUID keywordId) {
        Keyword keyword = keywordRepository.findById(keywordId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.KEYWORD_NOT_FOUMD));
        keywordRepository.delete(keyword);
    }
}
