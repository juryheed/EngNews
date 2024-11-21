package org.mjulikelion.engnews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mjulikelion.engnews.dto.request.keyword.KeywordDto;
import org.mjulikelion.engnews.dto.response.keyword.KeywordsListResponseDto;
import org.mjulikelion.engnews.entity.Category;
import org.mjulikelion.engnews.entity.Keyword;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.exception.ErrorCode;
import org.mjulikelion.engnews.exception.NotFoundException;
import org.mjulikelion.engnews.repository.CategoryRepository;
import org.mjulikelion.engnews.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordService {

    @Value("${naver.client.id}")
    private String clientId;

    //네이버 클라이언트 시크릿 번호
    @Value("${naver.client.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();


    private final KeywordRepository keywordRepository;
    private final CategoryRepository categoryRepository;

    public void saveKeyword(User user, KeywordDto keywordDto) {
        Category category = categoryRepository.findById(keywordDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        String translates=translateKeyword(keywordDto.getKeyword(),"ko","eg");

        //키워드 저장
        Keyword keyword = Keyword.builder()
                .category(category)
                .keyword(keywordDto.getKeyword())
                .translated(translates)
                .build();
        keywordRepository.save(keyword);
    }

    public KeywordsListResponseDto getKeyword(User user, UUID categoryId){
        Category category = categoryRepository.findByUserAndId(user,categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        List<Keyword> keywords = keywordRepository.findAllByCategory(category);

        return KeywordsListResponseDto.from(keywords);
    }

    public void deleteKeyword(User user, UUID keywordId) {
        Keyword keyword = keywordRepository.findById(keywordId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.KEYWORD_NOT_FOUMD));
        keywordRepository.delete(keyword);
    }

    private String translateKeyword(String text, String sourceLang, String targetLang){
        // API URL
        String apiUrl = "https://openapi.naver.com/v1/papago/n2mt";

        // 입력 텍스트와 언어 설정
        String encodedText = text;
        String body =  "source=ko&target=en&text=" +text;

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        // HTTP 요청 생성
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        // 결과 반환
        return response.getBody();
    }


}
