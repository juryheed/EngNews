package org.mjulikelion.engnews.controller.ai;

import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.authentication.AuthenticatedUser;
import org.mjulikelion.engnews.dto.ai.FeedbackDto;
import org.mjulikelion.engnews.dto.ai.TryDto;
import org.mjulikelion.engnews.dto.response.ResponseDto;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.service.SummarizeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/try-summarize")
public class SummarizeController {

    private final SummarizeService summarizeService;

    @PostMapping
    public ResponseEntity<ResponseDto<FeedbackDto>> trySummarize(@AuthenticatedUser User user,@RequestBody TryDto tryDto) {
        FeedbackDto feedback=summarizeService.trySummarize(user,tryDto);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "기사 요약하고 피드백 받기 성공", feedback));
    }
}
