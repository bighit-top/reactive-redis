package com.redis.rediscaching.fib.contoller;

import com.redis.rediscaching.fib.service.FibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("fib")
public class FibController {

    @Autowired
    private FibService fibService;

    @GetMapping("{index}/{name}")
    public Mono<Integer> getFib(@PathVariable int index, @PathVariable String name) {
        return Mono.fromSupplier(() -> this.fibService.getFib(index, name));
    }

    @GetMapping("{index}/clear") // 실제로는 PUT / POST / PATCH / DELETE 이 적합
    public Mono<Integer> clearCache(@PathVariable int index) {
        return Mono.fromRunnable(() -> this.fibService.clearCache(index));
    }
}
