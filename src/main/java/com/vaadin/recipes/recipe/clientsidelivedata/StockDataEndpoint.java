package com.vaadin.recipes.recipe.clientsidelivedata;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Random;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class StockDataEndpoint {

    @GetMapping(path = "/stock-price", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getStockPrices() {
        Random random = new Random();
        return Flux
            .<String>generate(
                sink -> {
                    sink.next(BigDecimal.valueOf(random.nextInt(10000), 2).toString());
                }
            )
            .delayElements(Duration.ofMillis(500))
            .take(30);
    }
}
