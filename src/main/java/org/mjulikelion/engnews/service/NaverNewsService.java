package org.mjulikelion.engnews.service;

import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.exception.ErrorCode;
import org.mjulikelion.engnews.exception.NotFoundException;
import org.mjulikelion.engnews.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NaverNewsService {

    private final CategoryRepository categoryRepository;

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    public String getNaverNews(User user, UUID categoryId){

        RestTemplate restTemplate = new RestTemplate();

        //카테고리 찾기
        String category = categoryRepository.findByUserAndId(user,categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND)).getCategory();

        // URI 생성
        StringBuffer sb = new StringBuffer();
        sb.append("https://openapi.naver.com/v1/search/news.json?query=");
        sb.append(category);
        sb.append("&display=10&start=1&sort=sim");
        String url = sb.toString();

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
}
