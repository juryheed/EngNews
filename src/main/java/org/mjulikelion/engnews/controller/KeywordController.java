package org.mjulikelion.engnews.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.authentication.AuthenticatedUser;
import org.mjulikelion.engnews.dto.request.keyword.KeywordDto;
import org.mjulikelion.engnews.dto.response.ResponseDto;
import org.mjulikelion.engnews.dto.response.keyword.CategoryKeywordListResponseDto;
import org.mjulikelion.engnews.dto.response.keyword.KeywordOptionListResponseDto;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.service.KeywordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/keywords")
public class KeywordController {

    private final KeywordService keywordService;

    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseDto<KeywordOptionListResponseDto>> getAllKeywords(@PathVariable UUID categoryId){
        KeywordOptionListResponseDto keywordOptions = keywordService.getAllKeywords(categoryId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "키워드 조회 완료", keywordOptions), HttpStatus.OK);
    }

    //키워드 단건 추가 컨트롤러
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> saveKeyword(@AuthenticatedUser User user, @RequestBody @Valid KeywordDto keyword) {
        keywordService.saveKeyword(user, keyword);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "키워드 저장 완료"), HttpStatus.CREATED);
    }

    //특정 카테고리와 관련된 키워드 전체 조회 컨트롤러
    @GetMapping("/user/{categoryId}")
    public ResponseEntity<ResponseDto<CategoryKeywordListResponseDto>> getKeyword(@AuthenticatedUser User user,@PathVariable UUID categoryId){
        CategoryKeywordListResponseDto keywords = keywordService.getKeyword(user,categoryId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "키워드 조회 완료",keywords), HttpStatus.OK);
    }

    //키워드 단건삭제 컨트롤러
    @DeleteMapping("/{keywordId}")
    public ResponseEntity<ResponseDto<Void>> deleteKeyword(@AuthenticatedUser User user, @PathVariable UUID keywordId){
        keywordService.deleteKeyword(user,keywordId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "키워드 삭제 완료"), HttpStatus.OK);
    }
}
