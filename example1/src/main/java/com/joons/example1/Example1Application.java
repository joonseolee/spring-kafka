package com.joons.example1;

import com.joons.example1.common.Foo2;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@SpringBootApplication
public class Example1Application {

    private final TaskExecutor exec = new SimpleAsyncTaskExecutor();

    public static void main(String[] args) {
        SpringApplication.run(Example1Application.class, args);
    }

    @Bean
    public SeekToCurrentErrorHandler errorHandler(KafkaOperations<Object, Object> template) {
        return new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer(template), new FixedBackOff(1000L, 2));
    }

    @Bean
    public RecordMessageConverter converter() {
        return new StringJsonMessageConverter();
    }

    @KafkaListener(id = "fooGroup", topics = "topic1")
    public void listen(Foo2 foo) {
        log.info("Received: " + foo);
        if (foo.getFoo().startsWith("fail"))
            throw new RuntimeException("failed");

        this.exec.execute(() -> log.info("Hit Enter to terminate..."));
    }

    @KafkaListener(id = "dltGroup", topics = "topic1.DLT")
    public void dltListen(String in) {
        log.info("Received from DLT: " + in);
        this.exec.execute(() -> log.info("Hit Enter to terminate...in dlt"));
    }

    @Bean
    public NewTopic topic() {
        return new NewTopic("topic1", 1, (short) 1);
    }
}
