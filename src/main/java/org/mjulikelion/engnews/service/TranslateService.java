package org.mjulikelion.engnews.service;

import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.dto.ai.FeedbackDto;
import org.mjulikelion.engnews.dto.ai.TryDto;
import org.mjulikelion.engnews.entity.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TranslateService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String translateURL = "http://43.203.141.103:8000/try-translate";

    public FeedbackDto tryTranslate(User user, TryDto tryDto){

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //Json형식
        headers.add("user", String.valueOf(user.getId()));

        // HTTP 요청 엔터티 생성 (헤더 + 바디)
        HttpEntity<TryDto> entity = new HttpEntity<>(tryDto, headers);

        // POST 요청 전송
        ResponseEntity<String> response = restTemplate.exchange(translateURL, HttpMethod.POST, entity, String.class);


        FeedbackDto feedback = FeedbackDto.builder()
                .gpt_answer(response.getBody())
                .build();

        return feedback;
    }
}
