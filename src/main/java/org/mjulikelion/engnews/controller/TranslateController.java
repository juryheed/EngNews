package org.mjulikelion.engnews.controller;

import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.authentication.AuthenticatedUser;
import org.mjulikelion.engnews.dto.ai.FeedbackDto;
import org.mjulikelion.engnews.dto.ai.TranslateDto;
import org.mjulikelion.engnews.dto.ai.TryDto;
import org.mjulikelion.engnews.dto.response.ResponseDto;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.service.TranslateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TranslateController {
    private final TranslateService translateService;

    @PostMapping("/try-translate")
    public ResponseEntity<ResponseDto<FeedbackDto>> tryTranslate(@AuthenticatedUser User user, @RequestBody TryDto tryDto) {
        FeedbackDto feedback=translateService.tryTranslate(user,tryDto);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "기사 번역하고 피드백 받기 성공", feedback));
    }

    @PostMapping("/translate")
    public ResponseEntity<ResponseDto<FeedbackDto>> translate(@AuthenticatedUser User user, @RequestBody TranslateDto translateDto){
        FeedbackDto feedback=translateService.translate(user,translateDto);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "기사를 한글↔영어 통번역 성공",feedback));
    }
}
