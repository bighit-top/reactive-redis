package com.redis.rediscaching.fib.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class FibService {

    private final static Logger log = Logger.getGlobal();

    // 캐시 저장, 조회
    // have a strategy for cache evict
    @Cacheable(value = "math:fib", key = "#index") //value=cachename, key=key (파라미터를 키로 캐시를 저장)
    public int getFib(int index, String name) {
        log.info("calculating fib for " + index + ", name: " + name);
        return this.fib(index);
    }

    // 캐시 삭제 - 단건
    // PUT / POST / PATCH / DELETE
    @CacheEvict(value = "math:fib", key = "#index")
    public void clearCache(int index) {
        log.info("clearing hash key");
    }

    // 캐시 삭제 - 다건
    @Scheduled(fixedRate = 10_000) // 10초 마다 반복 실행
    @CacheEvict(value = "math:fib", allEntries = true)
    public void clearCache() {
        log.info("clearing all fib keys");
    }

    private int fib(int index) {
        if (index < 2) {
            return index;
        }
        return fib(index - 1) + fib(index - 2);
    }
}
