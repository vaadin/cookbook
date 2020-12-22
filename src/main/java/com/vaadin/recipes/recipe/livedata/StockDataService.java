package com.vaadin.recipes.recipe.livedata;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Random;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class StockDataService {

    public Flux<BigDecimal> getStockPrice(String ticker) {
        Random random = new Random();
        return Flux
            .<BigDecimal>generate(
                sink -> {
                    sink.next(BigDecimal.valueOf(random.nextInt(10000), 2));
                }
            )
            .delayElements(Duration.ofMillis(500))
            .take(30);
    }
}
