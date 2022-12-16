package com.redis.rediscaching.city.service;

import com.redis.rediscaching.city.client.CityClient;
import com.redis.rediscaching.city.dto.City;
import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityClient cityClient;

    private RMapReactive<String, City> cityMap;
//    private RMapCacheReactive<String, City> cityMap;

    public CityService(RedissonReactiveClient client) {
        this.cityMap = client.getMap("city", new TypedJsonJacksonCodec(String.class, City.class));
//        this.cityMap = client.getMapCache("city", new TypedJsonJacksonCodec(String.class, City.class));
    }

    /**
     * get from cache
     * if empty - get from db / source
     *            put it in cache
     * return
     */
/*
    // 캐시 조회, 저장 : @Cacheable
    public Mono<City> getCity(final String zipCode) {
        return this.cityMap.get(zipCode)
                .switchIfEmpty(
                        this.cityClient.getCity(zipCode)
                                .flatMap(c -> this.cityMap.fastPut(zipCode, c).thenReturn(c))
                );
    }
*/

/*
    // 캐시를 주기적으로 삭제 : @CacheEvict
    public Mono<City> getCity(final String zipCode) {
        return this.cityMap.get(zipCode)
                .switchIfEmpty(
                        this.cityClient.getCity(zipCode)
                                .flatMap(c -> this.cityMap.fastPut(zipCode, c, 10, TimeUnit.SECONDS).thenReturn(c))
                );
    }
*/

    // 캐시 조회
    public Mono<City> getCity(final String zipCode) {
        return this.cityMap.get(zipCode)
//                .switchIfEmpty(this.cityClient.getCity(zipCode)) // 주기적으로 데이터를 채워놓는 경우 필요없음
                .onErrorResume(ex -> this.cityClient.getCity(zipCode));
    }

    // 캐시를 주기적으로 업데이트
    @Scheduled(fixedRate = 10_000)
    public void updateCity() {
        this.cityClient.getAll()
                .collectList()
                .map(list -> list.stream().collect(Collectors.toMap(City::getZip, Function.identity())))
                .flatMap(this.cityMap::putAll)
                .subscribe();
    }
}
