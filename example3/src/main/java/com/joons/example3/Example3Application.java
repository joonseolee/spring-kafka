package com.joons.example3;

import com.joons.example3.common.Foo2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerGroupMetadata;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

@Slf4j
@SpringBootApplication
public class Example3Application {

    public static final CountDownLatch LATCH = new CountDownLatch(1);
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(Example3Application.class, args);
        LATCH.await();
        Thread.sleep(5_000);
        context.close();
    }

    @Bean
    public RecordMessageConverter converter() {
        return new StringJsonMessageConverter();
    }

    @Bean
    public BatchMessagingMessageConverter batchConverter() {
        return new BatchMessagingMessageConverter(converter());
    }

    @KafkaListener(id = "fooGroup2", topics = "topic2")
    public void listen1(List<Foo2> foos) throws IOException {
        log.info("Received fooGroup2: " + foos);
        foos.forEach(f -> kafkaTemplate.send("topic2", f.getFoo().toUpperCase(Locale.ROOT)));
        log.info("Messages sent, hit enter to commit");
        System.in.read();
    }

    @KafkaListener(id = "fooGroup3", topics = "topic3")
    public void listen2(List<String> in) {
        log.info("Received fooGroup3: " + in);
        LATCH.countDown();
    }

    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name("topic2").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic topic3() {
        return TopicBuilder.name("topic3").partitions(1).replicas(1).build();
    }
}
