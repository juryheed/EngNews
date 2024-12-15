package org.mjulikelion.engnews.controller;

import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.authentication.AuthenticatedUser;
import org.mjulikelion.engnews.dto.ai.E2kResponseDto;
import org.mjulikelion.engnews.dto.ai.E2kTranslateDto;
import org.mjulikelion.engnews.dto.ai.FeedbackDto;
import org.mjulikelion.engnews.dto.ai.TryDto;
import org.mjulikelion.engnews.dto.response.ResponseDto;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.service.TranslateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class TranslateController {
    private final TranslateService translateService;

    @PostMapping("/try-translate")
    public ResponseEntity<ResponseDto<FeedbackDto>> tryTranslate(@AuthenticatedUser User user, @RequestBody TryDto tryDto) {
        FeedbackDto feedback=translateService.tryTranslate(user,tryDto);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "기사 번역하고 피드백 받기 성공", feedback));
    }

    @PostMapping("/translate_t5_e2k")
    public ResponseEntity<ResponseDto<E2kResponseDto>> e2kTranslate(@AuthenticatedUser User user, @RequestBody E2kTranslateDto e2kTranslateDto) {
        E2kResponseDto e2kResponseDto = translateService.e2kTranslate(user,e2kTranslateDto);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "영어에서 한국어로 통번역하기 성공", e2kResponseDto));
    }
}
