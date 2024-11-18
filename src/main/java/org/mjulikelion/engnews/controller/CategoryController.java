package org.mjulikelion.engnews.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.authentication.AuthenticatedUser;
import org.mjulikelion.engnews.dto.request.category.CategoryListDto;
import org.mjulikelion.engnews.dto.response.ResponseDto;
import org.mjulikelion.engnews.dto.response.category.NaverCategoryListResponseDto;
import org.mjulikelion.engnews.dto.response.category.UserCategoryListResponseDto;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/naver")
    public ResponseEntity<ResponseDto<NaverCategoryListResponseDto>> getNaverCategories() throws IOException {
        NaverCategoryListResponseDto categories = categoryService.getAllNaverCategories();
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "네이버 카테고리 리스트 조회 완료", categories), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Void>> saveCategories(@AuthenticatedUser User user, @RequestBody @Valid CategoryListDto categories) {
        categoryService.saveCategories(user, categories);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "카테고리 저장 완료"), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<UserCategoryListResponseDto>> getUserCategories(@AuthenticatedUser User user) {
        UserCategoryListResponseDto categories = categoryService.getAllUserCategories(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "유저 카테고리 리스트 조회 완료", categories), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ResponseDto<Void>> deleteCategory(@AuthenticatedUser User user, @PathVariable UUID categoryId) {
        categoryService.deleteCategory(user, categoryId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "카테고리 삭제 완료"), HttpStatus.OK);
    }
}
