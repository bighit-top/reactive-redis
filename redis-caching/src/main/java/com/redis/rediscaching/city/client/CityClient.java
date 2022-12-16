package com.redis.rediscaching.city.client;

import com.redis.rediscaching.city.dto.City;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CityClient {

    private final WebClient webClient;

    public CityClient(@Value("${city.service.url}") String url) {
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    // 단건 호출
    public Mono<City> getCity(final String zipCode) {
        return this.webClient
                .get()
                .uri("{zipcode}", zipCode)
                .retrieve()
                .bodyToMono(City.class);
    }

    // 다건 호출
    public Flux<City> getAll() {
        return this.webClient
                .get()
                .retrieve()
                .bodyToFlux(City.class);
    }
}
