package org.mjulikelion.engnews.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.mjulikelion.engnews.dto.response.article.ArticleDto;
import org.mjulikelion.engnews.dto.response.article.CategoryArticleDto;
import org.mjulikelion.engnews.entity.ArticleLike;
import org.mjulikelion.engnews.entity.Category;
import org.mjulikelion.engnews.entity.Keyword;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.exception.ErrorCode;
import org.mjulikelion.engnews.exception.UnauthorizedException;
import org.mjulikelion.engnews.repository.ArticleLikeRepository;
import org.mjulikelion.engnews.repository.CategoryRepository;
import org.mjulikelion.engnews.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NYTNewsService {

    @Value("${nyt.client.key}")
    private String clientId;

    private final RestTemplate restTemplate = new RestTemplate();
    private final KeywordRepository keywordRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleLikeService articleLikeService;
    private final NaverNewsService naverNewsService;
    private final ArticleLikeRepository articleLikeRepository;

    // 키워드 별 NYT 뉴스 목록 조회
    public List<CategoryArticleDto> getNYTNewsByKeyword(User user, String sort) {
        List<Category> categories = categoryRepository.findAllByUser(user);
        List<Keyword> keywords = keywordRepository.findAllByCategoryIn(categories);
        List<CategoryArticleDto> allArticles = new ArrayList<>();

        if (sort == null || sort.isEmpty()) {
            sort = "newest";
        }

        for (Keyword keyword : keywords) {
            String keywordName = keyword.getKeywordOptions().getKeywordName();
            String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json"
                    + "?q=" + URLEncoder.encode(keywordName, StandardCharsets.UTF_8)
                    + "&api-key=" + clientId
                    + "&sort=" + sort;

            JsonNode articlesNode = fetchNYTArticles(url);
            allArticles.addAll(parseArticles(articlesNode));
        }

        sortArticles(allArticles, sort);

        return allArticles;
    }

    // 카테고리 별 NYT 뉴스 목록 조회
    public List<CategoryArticleDto> getNYTByCategory(String category, int page, String sort) {
        if (sort == null || sort.isEmpty()) {
            sort = "newest";
        }

        String filter = category != null && !category.isEmpty()
                ? "section_name:(" + category + ")"
                : "";

        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json"
                + (filter.isEmpty() ? "" : "?fq=" + filter)
                + "&api-key=" + clientId
                + "&page=" + (page - 1)
                + "&sort=" + sort;

        JsonNode articlesNode = fetchNYTArticles(url);
        List<CategoryArticleDto> articles = parseArticles(articlesNode);

        sortArticles(articles, sort);

        return articles;
    }



    // 단건 기사 조회
    public ArticleDto getNYTNews(User user,String url) {
        String[] article = articleLikeService.getTitleImageAndContentFromUrl(url);
        String[] article2 = naverNewsService.getTimeAndJournalistNameFromUrl(url);

        List<ArticleLike> articleLikes = articleLikeRepository.findAllByUser(user);
        List<String> urls = new ArrayList<>();

        // for 루프를 사용하여 각 ArticleLike의 original_url을 추출
        for (ArticleLike articleLike : articleLikes) {
            urls.add(articleLike.getOriginalUrl());
        }

        boolean isArticleLike = urls.contains(url);

        System.out.print(urls);

        return ArticleDto.from(
                article[0],
                article[1],
                article[2],
                article2[0],
                article2[1]
                ,isArticleLike
        );
    }

    // NYT 뉴스 top5 조회하기
    public List<CategoryArticleDto> getTop5NYTNews() {
        String url = "https://api.nytimes.com/svc/mostpopular/v2/viewed/1.json?api-key=" + clientId;
        List<CategoryArticleDto> top5Articles = new ArrayList<>();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode articlesNode = rootNode.path("results");

            for (int i = 0; i < Math.min(5, articlesNode.size()); i++) {
                JsonNode article = articlesNode.get(i);

                String title = article.path("title").asText();
                String link = article.path("url").asText();
                String imageUrl = crawlImageUrlFromArticle(link);

                top5Articles.add(CategoryArticleDto.from(title, link, imageUrl));
            }
        } catch (IOException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_ARTICLE);
        }
        return top5Articles;
    }


    // NYT API 호출과 응답 처리
    private JsonNode fetchNYTArticles(String url) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(response.getBody()).path("response").path("docs");
        } catch (IOException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_ARTICLE);
        }
    }

    // NYT 기사 JSON 파싱
    private List<CategoryArticleDto> parseArticles(JsonNode articlesNode) {
        List<CategoryArticleDto> articles = new ArrayList<>();
        for (JsonNode article : articlesNode) {
            String title = article.path("headline").path("main").asText();
            String link = article.path("web_url").asText();
            String imageUrl = crawlImageUrlFromArticle(link);

            articles.add(CategoryArticleDto.from(title, link, imageUrl));
        }
        return articles;
    }

    // 이미지 url 크롤링 메서드
    private String crawlImageUrlFromArticle(String articleLink) {
        try {
            Document doc = Jsoup.connect(articleLink).get();
            Elements metaTags = doc.select("meta[property=og:image]");
            if (!metaTags.isEmpty()) {
                return metaTags.first().attr("content");
            } else {
                return "https://via.placeholder.com/150";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "https://via.placeholder.com/150";
        }
    }

    // 기사 정렬 방식
    private void sortArticles(List<CategoryArticleDto> articles, String sort) {
        if ("relevance".equals(sort)) {
            articles.sort(Comparator.comparingInt(a -> a.getTitle().length()));
        }
    }
}
