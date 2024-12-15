package org.mjulikelion.engnews.controller.ai;

import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.authentication.AuthenticatedUser;
import org.mjulikelion.engnews.dto.ai.request.AnalyzeSentenceDto;
import org.mjulikelion.engnews.dto.ai.response.FeedbackDto;
import org.mjulikelion.engnews.dto.response.ResponseDto;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.service.ai.AnalyzeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnalyzeController {
    private final AnalyzeService analyzeService;

    @PostMapping("/analyze-sentence")
    public ResponseEntity<ResponseDto<FeedbackDto>> analyzeSentence(@AuthenticatedUser User user, @RequestBody AnalyzeSentenceDto analyzeSentenceDto) {
        FeedbackDto feedback=analyzeService.analyzeSentence(user,analyzeSentenceDto);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "문장 안에 있는 숙어 및 주요 단어 분석 성공", feedback));
    }
}
