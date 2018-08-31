package com.imie.test.load;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
public class LoadTestController {

    @RequestMapping("/loadme")
    public void loadme() throws InterruptedException {

        final int min = 50;
        final int max = 500;
        int randomSleepTime = ThreadLocalRandom.current().nextInt(min, max + 1);
        Thread.sleep(randomSleepTime);
    }
}
