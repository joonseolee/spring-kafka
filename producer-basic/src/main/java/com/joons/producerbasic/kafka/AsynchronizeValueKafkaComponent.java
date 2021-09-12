package com.joons.producerbasic.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Properties;
import java.util.stream.IntStream;

/**
 * 비동기로 리턴을 기다리지않고 콜백형식으로 처리
 */
@Slf4j
//@Service
public class AsynchronizeValueKafkaComponent implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092");
        props.put("acks", "1");
        props.put("compression.type", "gzip");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);

        IntStream.rangeClosed(1, 10).forEach(number -> {
            try {
                producer.send(new ProducerRecord<String, String>("test", "async 테스트 " + number), new TestCallback());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        producer.close();
    }

    public static class TestCallback implements Callback {
        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            if (metadata != null) {
                log.info("Partition: {}, offset: {}", metadata.partition(), metadata.offset());
            } else exception.printStackTrace();
        }
    }
}
