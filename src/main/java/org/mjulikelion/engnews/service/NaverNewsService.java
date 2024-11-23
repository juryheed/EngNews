package org.mjulikelion.engnews.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mjulikelion.engnews.dto.request.article.ArticleRequestDto;
import org.mjulikelion.engnews.dto.response.article.ArticleDto;
import org.mjulikelion.engnews.dto.response.article.CategoryArticleDto;
import org.mjulikelion.engnews.entity.Category;
import org.mjulikelion.engnews.entity.Keyword;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.exception.ErrorCode;
import org.mjulikelion.engnews.exception.UnauthorizedException;
import org.mjulikelion.engnews.repository.CategoryRepository;
import org.mjulikelion.engnews.repository.KeywordRepository;
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

@Service
@RequiredArgsConstructor
public class NaverNewsService {

    //네이버 클라이언트 아이디
    @Value("${naver.client.id}")
    private String clientId;

    //네이버 클라이언트 시크릿 번호
    @Value("${naver.client.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    private final KeywordRepository keywordRepository;
    private final CategoryRepository categoryRepository;

    private final ArticleLikeService articleLikeService;

    //키워드로 네이버 뉴스 크롤링하기
    public List<CategoryArticleDto> getNewsByKeyword(User user) {
        List<Category> categories = categoryRepository.findAllByUser(user); // 유저 카테고리 조회
        List<Keyword> keywords = keywordRepository.findAllByCategoryIn(categories); // 카테고리별 키워드 조회

        List<CategoryArticleDto> allArticles = new ArrayList<>();

        for (Keyword keyword : keywords) {
            String keywordName = keyword.getKeyword();

            // URI 생성
            StringBuffer sb = new StringBuffer();
            sb.append("https://openapi.naver.com/v1/search/news.json?query=");
            sb.append(keywordName);
            sb.append("&display=10&start=1&sort=sim");
            String url = sb.toString();

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // API 호출
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // Jackson을 사용하여 JSON 파싱
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode items = rootNode.path("items");

                // 기사 제목과 링크와 이미지를 리스트에 추가
                for (JsonNode item : items) {
                    String title = item.path("title").asText();
                    String link = item.path("link").asText();
                    String imageUrl = crawlImageUrlFromArticle(link);

                    CategoryArticleDto article = CategoryArticleDto.from(title, link, imageUrl);

                    // 리스트에 추가
                    allArticles.add(article);
                }
            } catch (Exception e) {
                System.err.println("JSON 파싱 오류: " + e.getMessage());
            }
        }
        // ArticleListDto로 반환
        return allArticles;
    }

    public String crawlImageUrlFromArticle(String articleLink) {
        try {
            // 기사 페이지로 HTTP 요청 보내기
            Document doc = Jsoup.connect(articleLink).get();

            // 이미지 URL을 추출할 수 있는 부분을 찾기
            Elements metaTags = doc.select("meta[property=og:image]");

            // 첫 번째 <meta> 태그에서 'content' 속성값을 가져옴 (대표 이미지 URL)
            if (!metaTags.isEmpty()) {
                String imageUrl = metaTags.first().attr("content");
                return imageUrl;
            } else {
                // 대표 이미지가 없으면, 기본 이미지 URL을 반환
                return "https://via.placeholder.com/150"; // 기본 이미지
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "https://via.placeholder.com/150"; // 오류 발생 시 기본 이미지
        }
    }


    public List<CategoryArticleDto> getArticlesByCategory(String category, int page) {
        int display = 10;
        int start = (page - 1) * display + 1;

        String url = "https://openapi.naver.com/v1/search/news.json?query=" + category
                + "&display=" + display
                + "&start=" + start
                + "&sort=date";

        List<CategoryArticleDto> articles = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode items = rootNode.path("items");

            for (JsonNode item : items) {
                String title = item.path("title").asText();
                String link = item.path("link").asText();
                String imageUrl = crawlImageUrlFromArticle(link);

                articles.add(CategoryArticleDto.from(title, link, imageUrl));
            }
        } catch (IOException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_ARTICLE);
        }

        return articles;
    }


    //네이버 뉴스 단건 조회
    public ArticleDto getArticle(ArticleRequestDto articleRequestDto) {
        String url = articleRequestDto.getUrl();
        String[] article = articleLikeService.getTitleImageAndContentFromUrl(url);

        return ArticleDto.from(
                article[0],
                article[1],
                article[2]
        );
    }
}
