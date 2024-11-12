package org.mjulikelion.engnews;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class test {

    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
