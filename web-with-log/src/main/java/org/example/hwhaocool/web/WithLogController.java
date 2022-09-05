package org.example.hwhaocool.web;

import org.joda.time.DateTime;
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
public class WithLogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WithLogController.class);

    @GetMapping("/test2")
    public Map<String, String> test2() {

        LOGGER.debug("i'am debug from test2");
        LOGGER.info("you call test2");

        return Map.of("func", "test2", "time", DateTime.now().toString());
    }
}
