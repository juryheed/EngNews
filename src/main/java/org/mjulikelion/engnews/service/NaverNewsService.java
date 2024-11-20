package org.mjulikelion.engnews.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mjulikelion.engnews.dto.response.article.CategoryArticleDto;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    public List<CategoryArticleDto> getArticlesByCategory(String category) {
        String categoryUrl = mapCategoryToUrl(category);
        if (categoryUrl == null) return null;
        return crawlArticlesFromUrl(categoryUrl);
    }

    private String mapCategoryToUrl(String category) {
        return switch (category.toLowerCase()) {
            case "정치" -> "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=100";
            case "경제" -> "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=101";
            case "사회" -> "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=102";
            case "생활/문화" -> "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=103";
            case "세계" -> "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=104";
            case "IT/과학" -> "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=105";
            case "스포츠" -> "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=107";
            default -> null;
        };
    }

    private List<CategoryArticleDto> crawlArticlesFromUrl(String categoryUrl) {
        List<CategoryArticleDto> articles = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(categoryUrl).get();
            Elements items = doc.select("li.sa_item._SECTION_HEADLINE");
            for (Element item : items) {
                String title = item.select(".sa_text_strong").text();
                String link = item.select(".sa_thumb_inner a").attr("href");
                String imageUrl = item.select(".sa_thumb_inner img").attr("data-src");
                articles.add(CategoryArticleDto.from(title,link,imageUrl));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return articles;
    }
}
