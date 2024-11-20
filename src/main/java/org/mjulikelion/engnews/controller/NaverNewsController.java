package org.mjulikelion.engnews.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.dto.request.article.CategoryRequestDto;
import org.mjulikelion.engnews.dto.response.ResponseDto;
import org.mjulikelion.engnews.dto.response.article.CategoryArticleDto;
import org.mjulikelion.engnews.service.NaverNewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news/naver")
public class NaverNewsController {
    private final NaverNewsService naverNewsService;

    @GetMapping("/{keywordId}")
    public String getNaverNews(@PathVariable UUID keywordId) {
        return naverNewsService.getNaverNews(keywordId);
    }

    @GetMapping("/category")
    public ResponseEntity<ResponseDto<List<CategoryArticleDto>>> getNewsByCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        List<CategoryArticleDto> articles = naverNewsService.getArticlesByCategory(categoryRequestDto.getCategory());
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, categoryRequestDto.getCategory()+" 카테고리 기사 목록 조회 성공", articles));
    }

}
