package org.mjulikelion.engnews.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
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

    // 키워드로 NYT 뉴스 조회
    public List<CategoryArticleDto> getNYTNewsByKeyword(User user) {
        List<Category> categories = categoryRepository.findAllByUser(user);
        List<Keyword> keywords = keywordRepository.findAllByCategoryIn(categories);

        List<CategoryArticleDto> allArticles = new ArrayList<>();

        for (Keyword keyword : keywords) {
            String keywordName = keyword.getKeywordOptions().getKeywordName();

            String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json"
                    + "?q=" + keywordName
                    + "&api-key=" + clientId;

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode articles = rootNode.path("response").path("docs");

                for (JsonNode article : articles) {
                    String title = article.path("headline").path("main").asText();
                    String link = article.path("web_url").asText();
                    String imageUrl = crawlImageUrlFromArticle(link);

                    allArticles.add(CategoryArticleDto.from(title, link, imageUrl));
                }
            } catch (Exception e) {
                throw new UnauthorizedException(ErrorCode.INVALID_ARTICLE);
            }
        }
        return allArticles;
    }

    public List<CategoryArticleDto> getNYTByCategory(String category, int page) {
        String filter = "news_desk:(" + category + ")";
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json"
                + "?fq=" + filter
                + "&api-key=" + clientId
                + "&page=" + (page - 1); // NYT API는 페이지 번호가 0부터 시작

        List<CategoryArticleDto> articles = new ArrayList<>();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode articlesNode = rootNode.path("response").path("docs");

            for (JsonNode article : articlesNode) {
                String title = article.path("headline").path("main").asText();
                String link = article.path("web_url").asText();
                String imageUrl = crawlImageUrlFromArticle(link);

                articles.add(CategoryArticleDto.from(title, link, imageUrl));
            }
        } catch (IOException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_ARTICLE);
        }
        return articles;
    }


    // 단건 기사 조회
    public ArticleDto getNYTNews(String url) {
        String[] article = articleLikeService.getTitleImageAndContentFromUrl(url);
        String[] article2 = naverNewsService.getTimeAndJournalistNameFromUrl(url);
        return ArticleDto.from(
                article[0],
                article[1],
                article[2],
                article2[0],
                article2[1]
        );
    }


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
            e.printStackTrace();
            throw new UnauthorizedException(ErrorCode.INVALID_ARTICLE);
        }
        return top5Articles;
    }

}
