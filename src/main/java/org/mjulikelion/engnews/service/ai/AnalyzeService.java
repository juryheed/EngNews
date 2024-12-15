package org.mjulikelion.engnews.service.ai;


import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.dto.ai.request.AnalyzeSentenceDto;
import org.mjulikelion.engnews.dto.ai.response.FeedbackDto;
import org.mjulikelion.engnews.entity.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AnalyzeService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String analyze_Sentence = "http://43.203.141.103:8000/analyze-sentence";

    public FeedbackDto analyzeSentence(User user, AnalyzeSentenceDto analyzeSentenceDto){

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //Json형식
        headers.add("user", String.valueOf(user.getId()));

        // HTTP 요청 엔터티 생성 (헤더 + 바디)
        HttpEntity<AnalyzeSentenceDto> entity = new HttpEntity<>(analyzeSentenceDto, headers);

        // POST 요청 전송
        ResponseEntity<String> response = restTemplate.exchange(analyze_Sentence, HttpMethod.POST, entity, String.class);


        FeedbackDto feedback = FeedbackDto.builder()
                .gpt_answer(response.getBody())
                .build();

        return feedback;
    }
}
