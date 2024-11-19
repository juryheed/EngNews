package org.mjulikelion.engnews.service;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mjulikelion.engnews.dto.response.articleLike.ArticleLikeListResponseDto;
import org.mjulikelion.engnews.dto.response.articleLike.ArticleLikeResponseDto;
import org.mjulikelion.engnews.entity.ArticleLike;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.exception.ErrorCode;
import org.mjulikelion.engnews.exception.NotFoundException;
import org.mjulikelion.engnews.repository.ArticleLikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ArticleLikeService {

    private final ArticleLikeRepository articleLikeRepository;

    public ArticleLikeListResponseDto getAllArticleLike(User user) {
        List<ArticleLike> articleLikes = articleLikeRepository.findAllByUser(user);

        List<ArticleLikeResponseDto> articleLikeDtos = articleLikes.stream()
                .map(articleLike -> {
                    String[] titleAndImageAndContent = getTitleImageAndContentFromUrl(articleLike.getOriginal_url());
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

    private String[] getTitleImageAndContentFromUrl(String url) {
        String title = "";
        String imageUrl = "";
        String content = "";

        try {
            Document doc = Jsoup.connect(url).get();

            title = doc.select("meta[property=og:title]").attr("content");
            imageUrl = doc.select("meta[property=og:image]").attr("content");

//            content = doc.select("div.article-body").text();
            content = doc.select("article#dic_area.go_trans._article_content").text();
            if (content.isEmpty()) {
                content = doc.select("p").text();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String[] { title, imageUrl, content };
    }

    public void saveArticleLike(User user, String originalUrl) {
        ArticleLike articleLike = ArticleLike.builder()
                .user(user)
                .original_url(originalUrl)
                .build();
        articleLikeRepository.save(articleLike);
    }

    public void deleteArticleLikeById(User user, UUID articleLikeId) {
        ArticleLike articleLike = articleLikeRepository.findByUserAndId(user, articleLikeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ARTICLE_LIKE_NOT_FOUND));
        articleLikeRepository.delete(articleLike);
    }
}
