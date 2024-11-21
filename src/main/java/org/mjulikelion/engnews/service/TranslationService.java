package org.mjulikelion.engnews.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String LIBRETRANSLATE_URL = "https://libretranslate.com/translate";

    public String translate() {
        Map<String, Object> body = new HashMap<>();
        body.put("q", "사과");
        body.put("source", "ko");
        body.put("target", "en");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(LIBRETRANSLATE_URL, entity, Map.class);

        return response.getBody().get("translatedText").toString();
    }
}
