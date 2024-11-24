package org.mjulikelion.engnews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mjulikelion.engnews.dto.request.keyword.KeywordDto;
import org.mjulikelion.engnews.dto.response.keyword.CategoryKeywordListResponseDto;
import org.mjulikelion.engnews.entity.Category;
import org.mjulikelion.engnews.entity.Keyword;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.exception.ErrorCode;
import org.mjulikelion.engnews.exception.NotFoundException;
import org.mjulikelion.engnews.repository.CategoryRepository;
import org.mjulikelion.engnews.repository.KeywordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final CategoryRepository categoryRepository;

    public void saveKeyword(User user, KeywordDto keywordDto) {
        Category category = categoryRepository.findById(keywordDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
        //키워드 저장
        Keyword keyword = Keyword.builder()
                .category(category)
                .keyword(keywordDto.getKeyword())
                .build();
        keywordRepository.save(keyword);
    }

    public CategoryKeywordListResponseDto getKeyword(User user, UUID categoryId){
        Category category = categoryRepository.findByUserAndId(user,categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        List<Keyword> keywords = keywordRepository.findAllByCategory(category);

        return CategoryKeywordListResponseDto.from(keywords);
    }

    public void deleteKeyword(User user, UUID keywordId) {
        Keyword keyword = keywordRepository.findById(keywordId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.KEYWORD_NOT_FOUMD));
        keywordRepository.delete(keyword);
    }
}
