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
import org.mjulikelion.engnews.exception.ErrorCode;
import org.mjulikelion.engnews.exception.UnauthorizedException;
import org.mjulikelion.engnews.repository.ArticleLikeRepository;
import org.mjulikelion.engnews.repository.CategoryRepository;
import org.mjulikelion.engnews.repository.KeywordOptionsRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final KeywordOptionsRepository keywordOptionsRepository;
    private final ArticleLikeService articleLikeService;
    private final ArticleLikeRepository articleLikeRepository;

    //키워드로 네이버 뉴스 크롤링하기
    public List<CategoryArticleDto> getNewsByKeyword(User user) {
        List<Category> categories = categoryRepository.findAllByUser(user); // 유저 카테고리 조회
        List<Keyword> keywords = keywordRepository.findAllByCategoryIn(categories); // 카테고리별 키워드 조회

        List<CategoryArticleDto> allArticles = new ArrayList<>();

        for (Keyword keyword : keywords) {
            String keywordName = keyword.getKeywordOptions().getKeywordName();

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

    // 카테고리로 네이버 뉴스 가져오기
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
    public ArticleDto getArticle(User user, String url) {
        String[] article = articleLikeService.getTitleImageAndContentFromUrl(url);
        String[] article2 = getTimeAndJournalistNameFromUrl(url);

        List<ArticleLike> articleLikes = articleLikeRepository.findAllByUser(user);
        List<String> urls = new ArrayList<>();

        // for 루프를 사용하여 각 ArticleLike의 original_url을 추출
        for (ArticleLike articleLike : articleLikes) {
            urls.add(articleLike.getOriginal_url());
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



    // 관련 기사 목록 조회하기
    public List<RelatedArticleDto> getRelatedArticles(String articleUrl) {
        List<RelatedArticleDto> relatedArticles = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(articleUrl).get();
            Elements relatedNewsElements = doc.select("ul.ofhe_list li");
            for (Element element : relatedNewsElements) {
                String relatedTitle = element.text();
                String relatedLink = element.attr("href");
                if (!relatedLink.startsWith("http")) {
                    relatedLink = "https://news.naver.com" + relatedLink;
                }
                String imageUrl = crawlImageUrlFromArticle(relatedLink);
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

            Element firstRankingBox = doc.select(".rankingnews_box").first();   // 첫 번째 언론사의 top5 크롤링

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
            } else {
                System.err.println("첫 번째 rankingnews_box를 찾을 수 없습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("네이버 뉴스 랭킹 크롤링 실패");
        }
        return topRankingArticles;
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
}
