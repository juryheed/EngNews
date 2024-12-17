package org.mjulikelion.engnews.controller.ai;

import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.authentication.AuthenticatedUser;
import org.mjulikelion.engnews.dto.ai.request.TrySummarizeContentDto;
import org.mjulikelion.engnews.dto.ai.request.TrySummarizeMessageDto;
import org.mjulikelion.engnews.dto.ai.request.SummarizeDto;
import org.mjulikelion.engnews.dto.ai.response.FeedbackDto;
import org.mjulikelion.engnews.dto.response.ResponseDto;
import org.mjulikelion.engnews.entity.User;
import org.mjulikelion.engnews.service.ai.SummarizeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SummarizeController {

    private final SummarizeService summarizeService;

    @PostMapping("/try-summarize/content")
    public ResponseEntity<ResponseDto<FeedbackDto>> trySummarizeContent(@AuthenticatedUser User user, @RequestBody TrySummarizeContentDto trySummarizeContentDto) {
        FeedbackDto feedback=summarizeService.trySummarizeContent(user,trySummarizeContentDto);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "기사 요약하고 피드백 받기 성공(첫번째 통신)", feedback));
    }
    @PostMapping("/try-summarize/message")
    public ResponseEntity<ResponseDto<FeedbackDto>> trySummarizeMessage(@AuthenticatedUser User user,@RequestBody TrySummarizeMessageDto trySummarizeMessageDto) {
        FeedbackDto feedback=summarizeService.trySummarizeMessage(user,trySummarizeMessageDto);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "기사 요약하고 피드백 받기 성공(두번째 이상 통신)", feedback));
    }

    @PostMapping("/summarize")
    public ResponseEntity<ResponseDto<FeedbackDto>> summarize(@AuthenticatedUser User user, @RequestBody SummarizeDto summarizeDto) {
        FeedbackDto feedback = summarizeService.summarize(user,summarizeDto);
        return ResponseEntity.ok(ResponseDto.res(HttpStatus.OK, "요약해보기 성공", feedback));
    }
}
