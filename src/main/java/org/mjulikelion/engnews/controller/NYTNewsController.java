package org.mjulikelion.engnews.controller;

import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.authentication.AuthenticatedUser;
import org.mjulikelion.engnews.dto.response.ResponseDto;
import org.mjulikelion.engnews.dto.response.article.ArticleNytDto;
import org.mjulikelion.engnews.dto.response.article.CategoryArticleDto;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.service.NYTNewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news/nyt")
public class NYTNewsController {

    private final NYTNewsService nytService;

    @GetMapping("/keyword")
    public ResponseEntity<ResponseDto<List<CategoryArticleDto>>> getNYTNewsByKeyword(@AuthenticatedUser User user, @RequestParam String sort) {
        List<CategoryArticleDto> articles = nytService.getNYTNewsByKeyword(user, sort);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "유저 키워드 별 NYT 기사 목록 조회 성공", articles));
    }

    @GetMapping("/categories")
    public ResponseEntity<ResponseDto<List<CategoryArticleDto>>> getNYTByCategory(@RequestParam String category, @RequestParam int page, @RequestParam String sort) {
        List<CategoryArticleDto> articles = nytService.getNYTByCategory(category, page, sort);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, category+" 카테고리 별 NYT 기사 목록 조회 성공", articles));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<ArticleNytDto>> getNYTNews(@AuthenticatedUser User user, @RequestParam String url) {
        ArticleNytDto article = nytService.getNYTNews(user, url);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "NYT 기사 단건 조회 성공", article));
    }

    @GetMapping("/top5")
    public ResponseEntity<ResponseDto<List<CategoryArticleDto>>> getTop5NYTNews() {
        List<CategoryArticleDto> articles = nytService.getTop5NYTNews();
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "NYT 기사 TOP5 목록 조회 성공", articles));
    }
}
