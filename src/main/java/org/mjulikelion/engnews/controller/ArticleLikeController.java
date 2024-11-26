package org.mjulikelion.engnews.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.mjulikelion.engnews.authentication.AuthenticatedUser;
import org.mjulikelion.engnews.dto.request.articleLike.ArticleLikeUrlDto;
import org.mjulikelion.engnews.dto.response.ResponseDto;
import org.mjulikelion.engnews.dto.response.articleLike.ArticleLikeListResponseDto;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.service.ArticleLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/articles-like")
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @GetMapping("/naver")
    public ResponseEntity<ResponseDto<ArticleLikeListResponseDto>> getNaverArticleLikes(@AuthenticatedUser User user) {
        ArticleLikeListResponseDto articleLikeListResponseDto = articleLikeService.getNaverArticleLikes(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "네이버 뉴스 찜한 기사 리스트 조회 완료", articleLikeListResponseDto), HttpStatus.OK);
    }

    @GetMapping("/nyt")
    public ResponseEntity<ResponseDto<ArticleLikeListResponseDto>> getNytArticleLikes(@AuthenticatedUser User user) {
        ArticleLikeListResponseDto articleLikeListResponseDto = articleLikeService.getNytArticleLikes(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "NYT 뉴스 찜한 기사 리스트 조회 완료", articleLikeListResponseDto), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Void>> saveArticleLike(@AuthenticatedUser User user,
                                                             @RequestBody @Valid ArticleLikeUrlDto articleLikeUrlDto) {
        articleLikeService.saveArticleLike(user, articleLikeUrlDto.getOriginalUrl(), articleLikeUrlDto.getNews());
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "기사 찜하기 완료"), HttpStatus.CREATED);
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<ResponseDto<Void>> deleteArticleLikeById(@AuthenticatedUser User user, @PathVariable UUID articleId) {
        articleLikeService.deleteArticleLikeById(user, articleId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "찜한 뉴스 삭제 완료"), HttpStatus.OK);
    }
}
