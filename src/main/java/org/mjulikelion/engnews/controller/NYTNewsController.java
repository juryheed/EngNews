package org.mjulikelion.engnews.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.authentication.AuthenticatedUser;
import org.mjulikelion.engnews.dto.request.article.ArticleRequestDto;
import org.mjulikelion.engnews.dto.response.ResponseDto;
import org.mjulikelion.engnews.dto.response.article.ArticleDto;
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
    public ResponseEntity<ResponseDto<List<CategoryArticleDto>>> getNYTNewsByKeyword(@AuthenticatedUser User user) {
        List<CategoryArticleDto> articles = nytService.getNYTNewsByKeyword(user);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, " 키워드로 NYT 기사 목록 조회 성공", articles));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<CategoryArticleDto>>> getNYTByCategory(@RequestParam String category, @RequestParam int page) {
        List<CategoryArticleDto> articles = nytService.getNYTByCategory(category, page);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, category+" 카테고리 NYT 기사 목록 조회 성공", articles));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<ArticleDto>> getNYTNews(@RequestBody @Valid ArticleRequestDto articleRequestDto) {
        ArticleDto article=nytService.getNYTNews(articleRequestDto);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "NYT 기사 단건 조회 성공", article));
    }

    @GetMapping("/top5")
    public ResponseEntity<ResponseDto<List<CategoryArticleDto>>> getTop5NYTNews() {
        List<CategoryArticleDto> articles = nytService.getTop5NYTNews();
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "NYT 뉴스 top5 목록 조회 성공", articles));
    }
}
