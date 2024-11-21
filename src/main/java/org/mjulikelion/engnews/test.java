package org.mjulikelion.engnews;

import lombok.AllArgsConstructor;
import org.mjulikelion.engnews.service.TranslationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class test {

    private final TranslationService  translationService;

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @GetMapping("/transelate")
    public String translate() {
        return translationService.translate();
    }
}
