package org.mjulikelion.engnews.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.authentication.AuthenticatedUser;
import org.mjulikelion.engnews.dto.request.article.ArticleRequestDto;
import org.mjulikelion.engnews.dto.response.ResponseDto;
import org.mjulikelion.engnews.dto.response.article.ArticleDto;
import org.mjulikelion.engnews.dto.response.article.CategoryArticleDto;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.service.NaverNewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news/naver")
public class NaverNewsController {
    private final NaverNewsService naverNewsService;

    @GetMapping("/keyword")
    public ResponseEntity<ResponseDto<List<CategoryArticleDto>>> getNaverNewsByKeyword(@AuthenticatedUser User user) {
        List<CategoryArticleDto> articles = naverNewsService.getNewsByKeyword(user);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, " 키워드로 기사 목록 조회 성공", articles));
    }

    @GetMapping("/categories")
    public ResponseEntity<ResponseDto<List<CategoryArticleDto>>> getNewsByCategory(@RequestParam String category, @RequestParam int page) {
        List<CategoryArticleDto> articles = naverNewsService.getArticlesByCategory(category, page);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, category+" 카테고리 기사 목록 조회 성공", articles));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<ArticleDto>> getNews(@RequestParam String url) {
        ArticleDto article=naverNewsService.getArticle(url);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "기사 단건 조회 성공", article));
    }

    @GetMapping("/top5")
    public ResponseEntity<ResponseDto<List<CategoryArticleDto>>> getTop5NaverNews() {
        List<CategoryArticleDto> articles = naverNewsService.getTop5NaverNews();
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "네이버 뉴스 top5 목록 조회 성공", articles));
    }
}
