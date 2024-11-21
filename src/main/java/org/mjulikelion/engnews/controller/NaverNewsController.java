package org.mjulikelion.engnews.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.authentication.AuthenticatedUser;
import org.mjulikelion.engnews.dto.request.article.CategoryRequestDto;
import org.mjulikelion.engnews.dto.response.ResponseDto;
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

    @GetMapping("/category")
    public ResponseEntity<ResponseDto<List<CategoryArticleDto>>> getNewsByCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        List<CategoryArticleDto> articles = naverNewsService.getArticlesByCategory(categoryRequestDto.getCategory());
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, categoryRequestDto.getCategory()+" 카테고리 기사 목록 조회 성공", articles));
    }

}
