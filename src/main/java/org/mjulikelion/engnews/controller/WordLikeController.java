package org.mjulikelion.engnews.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.authentication.AuthenticatedUser;
import org.mjulikelion.engnews.dto.request.CreateWordLikeDto;
import org.mjulikelion.engnews.dto.response.ResponseDto;
import org.mjulikelion.engnews.dto.response.WordLikeListResponseData;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.service.WordLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/words-like")
public class WordLikeController {
    private final WordLikeService wordLikeService;

    // 단어 찜하기
    @PostMapping()
    public ResponseEntity<ResponseDto<Void>> createWordLike(@AuthenticatedUser User user, @RequestBody @Valid CreateWordLikeDto createWordLikeDto) {
        wordLikeService.createWordLike(user, createWordLikeDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "단어 찜하기 완료"), HttpStatus.CREATED);
    }

    // 찜한 단어 조회하기
    @GetMapping()
    public ResponseEntity<ResponseDto<WordLikeListResponseData>> getAllWordLike(@AuthenticatedUser User user) {
        WordLikeListResponseData wordLikeListResponseData = wordLikeService.getAllWordLike(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "찜한 단어 조회 완료", wordLikeListResponseData), HttpStatus.OK);
    }

    // 찜한 단어 삭제하기
    @DeleteMapping("/{wordId}")
    public ResponseEntity<ResponseDto<Void>> deleteWordLikeById(@AuthenticatedUser User user, @PathVariable UUID wordId) {
        wordLikeService.deleteWordLikeById(user, wordId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "찜한 단어 삭제 완료"), HttpStatus.OK);
    }

}
