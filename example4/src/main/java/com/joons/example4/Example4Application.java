package com.joons.example4;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;

@Slf4j
@SpringBootApplication
public class Example4Application {

    public static void main(String[] args) {
        SpringApplication.run(Example4Application.class, args);
    }

    @RetryableTopic(attempts = "5", backoff = @Backoff(delay = 2_000, maxDelay = 10_000, multiplier = 2))
    @KafkaListener(id = "fooGroup", topics = "topic4")
    public void listen(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("Received: {} from {} @ {}", in, topic, offset);

        if (in.startsWith("fail"))
            throw new RuntimeException("failed");
    }

    @DltHandler
    public void listenDlt(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                          @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("DLT Received: {} from {} @ {}", in, topic, offset);
    }
}
