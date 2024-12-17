package org.mjulikelion.engnews.service.ai;

import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.dto.ai.response.FeedbackDto;
import org.mjulikelion.engnews.dto.ai.request.TrySummarizeDto;
import org.mjulikelion.engnews.entity.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SummarizeService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String trySummarizeURL = "http://13.209.83.200:8000/try-summarize";

    public FeedbackDto trySummarize(User user, TrySummarizeDto trySummarizeDto){

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //Json형식
        headers.add("user", String.valueOf(user.getId()));

        // HTTP 요청 엔터티 생성 (헤더 + 바디)
        HttpEntity<TrySummarizeDto> entity = new HttpEntity<>(trySummarizeDto, headers);

        // POST 요청 전송
        ResponseEntity<String> response = restTemplate.exchange(trySummarizeURL, HttpMethod.POST, entity, String.class);


        FeedbackDto feedback = FeedbackDto.builder()
                .gpt_answer(response.getBody())
                .build();

        return feedback;
    }
}
