package org.mjulikelion.engnews.service;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mjulikelion.engnews.dto.response.article.ArticleNytDto;
import org.mjulikelion.engnews.dto.response.articleLike.ArticleLikeListResponseDto;
import org.mjulikelion.engnews.dto.response.articleLike.ArticleLikeResponseDto;
import org.mjulikelion.engnews.entity.ArticleLike;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.exception.ErrorCode;
import org.mjulikelion.engnews.exception.NotFoundException;
import org.mjulikelion.engnews.repository.ArticleLikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository;
    private final NYTNewsService nytNewsService;

    public ArticleLikeListResponseDto getNaverArticleLikes(User user) {
        List<ArticleLike> articleLikes = articleLikeRepository.findAllByUserAndNews(user, "naver");

        List<ArticleLikeResponseDto> articleLikeDtos = articleLikes.stream()
                .map(articleLike -> {
                    String[] titleAndImageAndContent = getTitleImageAndContentFromUrl(articleLike.getOriginalUrl());
                    return ArticleLikeResponseDto.from(
                            articleLike,
                            titleAndImageAndContent[0],
                            titleAndImageAndContent[1],
                            titleAndImageAndContent[2]
                    );
                })
                .toList();

        return ArticleLikeListResponseDto.builder()
                .articleLikes(articleLikeDtos)
                .build();
    }

    public ArticleLikeListResponseDto getNytArticleLikes(User user) {
        List<ArticleLike> articleLikes = articleLikeRepository.findAllByUserAndNews(user, "nyt");

        List<ArticleLikeResponseDto> articleLikeDtos = articleLikes.stream()
                .map(articleLike -> {
                    // API 호출을 통해 제목, 이미지, 내용 가져오기
                    String url = articleLike.getOriginalUrl();
                    ArticleNytDto nytArticle = nytNewsService.getNYTNews(user, url);  // getNYTNews 사용하여 데이터 가져오기

                    // ArticleLikeResponseDto로 변환
                    return ArticleLikeResponseDto.from(
                            articleLike,
                            nytArticle.getTitle(),
                            nytArticle.getImageUrl(),
                            nytArticle.getContent()
                    );
                })
                .toList();

        return ArticleLikeListResponseDto.builder()
                .articleLikes(articleLikeDtos)
                .build();
    }



    public String[] getTitleImageAndContentFromUrl(String url) {
        String title = "";
        String imageUrl = "";
        String content = "";

        try {
            Document doc = Jsoup.connect(url).get();

            title = doc.select("meta[property=og:title]").attr("content");
            imageUrl = doc.select("meta[property=og:image]").attr("content");

            content = doc.select("article#dic_area.go_trans._article_content").text();
            if (content.isEmpty()) {
                content = doc.select("p").text();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String[] { title, imageUrl, content };
    }

    public void saveArticleLike(User user, String originalUrl, String news) {
        ArticleLike articleLike = ArticleLike.builder()
                .user(user)
                .originalUrl(originalUrl)
                .news(news)
                .build();
        articleLikeRepository.save(articleLike);
    }

    public void deleteArticleLikeById(User user, String url) {
        ArticleLike articleLike = articleLikeRepository.findByOriginalUrlAndUser( url,user)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ARTICLE_LIKE_NOT_FOUND));
        articleLikeRepository.delete(articleLike);
    }
}
