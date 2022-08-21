package com.github.hwhaocool;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author yellowtail
 * @since 2022/8/21 17:11
 */
@RestController
public class YellowController {

    @GetMapping("/test")
    public Map<String, String> test() {

        return Map.of("data", "ok");
    }
}
