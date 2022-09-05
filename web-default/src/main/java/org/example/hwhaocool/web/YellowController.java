package org.example.hwhaocool.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author yellowtail
 * @since 2022/8/21 17:11
 */
@RestController
public class YellowController {

    private static final Logger LOGGER = LoggerFactory.getLogger(YellowController.class);

    @GetMapping("/test1")
    public Map<String, String> test() {

        LOGGER.info("test1");

        return Map.of("data", "ok");
    }
}
