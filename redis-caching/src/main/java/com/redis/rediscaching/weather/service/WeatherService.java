package com.redis.rediscaching.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;
import java.util.stream.IntStream;

@Service
public class WeatherService {

    private final static Logger log = Logger.getGlobal();

    @Autowired
    private ExternalServiceClient client;

    @Cacheable("weather")
    public int getInfo(int zip) {
        return 0;
    }

    @Scheduled(fixedRate = 10_000)
    public void update() {
        log.info("updating weather");
        IntStream.rangeClosed(1, 5)
                .forEach(this.client::getWeatherInfo);
    }
}
