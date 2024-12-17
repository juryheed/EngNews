package org.mjulikelion.engnews.service.ai;

import lombok.RequiredArgsConstructor;
import org.mjulikelion.engnews.dto.ai.request.*;
import org.mjulikelion.engnews.dto.ai.response.E2kResponseDto;
import org.mjulikelion.engnews.dto.ai.response.FeedbackDto;
import org.mjulikelion.engnews.dto.ai.response.K2eResponseDto;
import org.mjulikelion.engnews.entity.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TranslateService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String try_TranslateURL = "http://13.209.83.200:8000/try-translate";
    private final String translateURL = "http://13.209.83.200:8000/translate";
    private final String e2kTranslateURL = "http://13.209.83.200:8000/translate_t5_e2k";
    private final String k2eTranslateURL = "http://13.209.83.200:8000/translate_t5_k2e";

    public FeedbackDto tryTranslateSentence(User user, TryTranslateSentenceDto tryTranslateSentenceDto) {

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //Json형식
        headers.add("user", String.valueOf(user.getId()));

        // HTTP 요청 엔터티 생성 (헤더 + 바디)
        HttpEntity<TryTranslateSentenceDto> entity = new HttpEntity<>(tryTranslateSentenceDto, headers);

        // POST 요청 전송
        ResponseEntity<String> response = restTemplate.exchange(try_TranslateURL, HttpMethod.POST, entity, String.class);


        FeedbackDto feedback = FeedbackDto.builder()
                .gpt_answer(response.getBody())
                .build();

        return feedback;
    }

    public FeedbackDto tryTranslateMessage(User user, TryTranslateMessageDto tryTranslateMessageDto) {

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //Json형식
        headers.add("user", String.valueOf(user.getId()));

        // HTTP 요청 엔터티 생성 (헤더 + 바디)
        HttpEntity<TryTranslateMessageDto> entity = new HttpEntity<>(tryTranslateMessageDto, headers);

        // POST 요청 전송
        ResponseEntity<String> response = restTemplate.exchange(try_TranslateURL, HttpMethod.POST, entity, String.class);


        FeedbackDto feedback = FeedbackDto.builder()
                .gpt_answer(response.getBody())
                .build();

        return feedback;
    }

    public FeedbackDto translate(User user, TranslateDto translateDto) {

        System.out.print(user);
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); //Json형식
        headers.add("user", String.valueOf(user.getId()));

        // HTTP 요청 엔터티 생성 (헤더 + 바디)
        HttpEntity<TranslateDto> entity = new HttpEntity<>(translateDto, headers);

        // POST 요청 전송
        ResponseEntity<String> response = restTemplate.exchange(translateURL, HttpMethod.POST, entity, String.class);

        FeedbackDto feedback = FeedbackDto.builder()
                .gpt_answer(response.getBody())
                .build();

        return feedback;
    }

    public E2kResponseDto e2kTranslate(User user, E2kTranslateDto e2kTranslateDto) {
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user", String.valueOf(user.getId()));

        // HTTP 요청 엔터티 생성 (헤더 + 바디)
        HttpEntity<E2kTranslateDto> entity = new HttpEntity<>(e2kTranslateDto, headers);

        // POST 요청 전송
        ResponseEntity<String> response = restTemplate.exchange(e2kTranslateURL, HttpMethod.POST, entity, String.class);

        return E2kResponseDto.builder()
                .answer(response.getBody())
                .build();
    }

    public K2eResponseDto k2eTranslate(User user, K2eTranslateDto k2eTranslateDto) {
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user", String.valueOf(user.getId()));

        // HTTP 요청 엔터티 생성 (헤더 + 바디)
        HttpEntity<K2eTranslateDto> entity = new HttpEntity<>(k2eTranslateDto, headers);

        // POST 요청 전송
        ResponseEntity<String> response = restTemplate.exchange(k2eTranslateURL, HttpMethod.POST, entity, String.class);

        return K2eResponseDto.builder()
                .answer(response.getBody())
                .build();
    }
}
