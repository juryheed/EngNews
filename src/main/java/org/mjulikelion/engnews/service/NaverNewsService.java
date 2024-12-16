package org.mjulikelion.engnews.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mjulikelion.engnews.dto.response.article.ArticleDto;
import org.mjulikelion.engnews.dto.response.article.CategoryArticleDto;
import org.mjulikelion.engnews.dto.response.article.RelatedArticleDto;
import org.mjulikelion.engnews.entity.*;
import org.mjulikelion.engnews.repository.ArticleLikeRepository;
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
    private final ArticleLikeRepository articleLikeRepository;

    //키워드로 네이버 뉴스 크롤링하기
    public List<CategoryArticleDto> getNewsByKeyword(User user, String sort) {
        List<Category> categories = categoryRepository.findAllByUser(user);
        List<Keyword> keywords = keywordRepository.findAllByCategoryIn(categories);
        List<CategoryArticleDto> allArticles = new ArrayList<>();

        HttpHeaders headers = createNaverApiHeaders();

        for (Keyword keyword : keywords) {
            String keywordName = keyword.getKeywordOptions().getKeywordName();
            String url = "https://openapi.naver.com/v1/search/news.json?query=" + keywordName
                    + "&display=10&start=1&sort=" + sort;
            allArticles.addAll(fetchArticlesFromApi(url, headers));
        }
        return allArticles;
    }

    // 카테고리로 네이버 뉴스 가져오기
    public List<CategoryArticleDto> getArticlesByCategory(String category, int page, String sort) {
        int display = 10;
        int start = (page - 1) * display + 1;

        String url = "https://openapi.naver.com/v1/search/news.json?query=" + category
                + "&display=" + display + "&start=" + start + "&sort=" + sort;

        HttpHeaders headers = createNaverApiHeaders();
        return fetchArticlesFromApi(url, headers);
    }

    //네이버 뉴스 단건 조회
    public ArticleDto getArticle(User user, String url) {
        String[] article = articleLikeService.getTitleImageAndContentFromUrl(url);
        String[] article2 = getTimeAndJournalistNameFromUrl(url);

        List<ArticleLike> articleLikes = articleLikeRepository.findAllByUser(user);
        List<String> urls = new ArrayList<>();

        // for 루프를 사용하여 각 ArticleLike의 original_url을 추출
        for (ArticleLike articleLike : articleLikes) {
            urls.add(articleLike.getOriginalUrl());
        }

        boolean isArticleLike = urls.contains(url);

        System.out.print("url:"+urls);

        return ArticleDto.from(
                article[0],
                article[1],
                article[2],
                article2[0],
                article2[1],
                isArticleLike
        );
    }

    // 관련 기사 목록 조회하기
    public List<RelatedArticleDto> getRelatedArticles(String articleUrl) {
        List<RelatedArticleDto> relatedArticles = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(articleUrl).get();
            Elements relatedNewsElements = doc.select("ul.ofhe_list li");
            for (Element element : relatedNewsElements) {
                String relatedTitle = element.select("a").text();

                String relatedLink = element.select("a").attr("href");
                if (!relatedLink.startsWith("http")) {
                    relatedLink = "https://news.naver.com" + relatedLink;
                }

                String imageUrl = crawlImageUrlFromArticle(relatedLink);
                if (imageUrl.isEmpty()) {
                    imageUrl = "https://via.placeholder.com/150";
                }

                relatedArticles.add(RelatedArticleDto.from(relatedTitle, relatedLink, imageUrl));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("관련 뉴스 크롤링 실패: " + e.getMessage());
        }
        return relatedArticles;
    }

    // 네이버 뉴스 top5 조회하기
    public List<CategoryArticleDto> getTop5NaverNews() {
        String url = "https://news.naver.com/main/ranking/popularDay.naver";
        List<CategoryArticleDto> topRankingArticles = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).get();
            Element firstRankingBox = doc.select(".rankingnews_box").first();

            if (firstRankingBox != null) {
                Elements rankingArticles = firstRankingBox.select(".rankingnews_list li");

                for (int i = 0; i < Math.min(5, rankingArticles.size()); i++) {
                    Element articleElement = rankingArticles.get(i);
                    String title = articleElement.select(".list_title").text();
                    String link = articleElement.select("a").attr("href");

                    if (!link.startsWith("http")) {
                        link = "https://news.naver.com" + link;
                    }

                    String imageUrl = crawlImageUrlFromArticle(link);
                    topRankingArticles.add(CategoryArticleDto.from(title, link, imageUrl));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("네이버 뉴스 랭킹 크롤링 실패");
        }
        return topRankingArticles;
    }

    // 기사 날짜랑 기자 추출 메서드
    protected String[] getTimeAndJournalistNameFromUrl(String url) {
        String time = "";
        String journalistName = "";

        try {
            Document doc = Jsoup.connect(url).get();

            time = doc.select("span.media_end_head_info_datestamp_time").attr("data-date-time");
            journalistName = String.valueOf(doc.selectFirst("em.media_end_head_journalist_name"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        journalistName=journalistName.replaceAll("<[^>]*>", "").trim();

        return new String[] { time, journalistName};
    }

    // API 호출 및 JSON 파싱 메서드
    private List<CategoryArticleDto> fetchArticlesFromApi(String url, HttpHeaders headers) {
        List<CategoryArticleDto> articles = new ArrayList<>();
        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
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
            e.printStackTrace();
            throw new RuntimeException("API 호출 및 파싱 실패");
        }
        return articles;
    }

    // 헤더 설정 메서드
    private HttpHeaders createNaverApiHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        return headers;
    }

    // 이미지 url 크롤링 메서드
    public String crawlImageUrlFromArticle(String articleLink) {
        try {
            Document doc = Jsoup.connect(articleLink).get();

            Elements metaTags = doc.select("meta[property=og:image]");

            if (!metaTags.isEmpty()) {
                String imageUrl = metaTags.first().attr("content");
                return imageUrl;
            } else {
                return "https://via.placeholder.com/150";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "https://via.placeholder.com/150";
        }
    }
}
