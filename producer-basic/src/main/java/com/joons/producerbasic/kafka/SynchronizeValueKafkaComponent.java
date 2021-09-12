package com.joons.producerbasic.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

/**
 * 메시지를 보내고 실제로 결과값이 어떤지 기다리고 확인
 */
@Slf4j
@Service
public class SynchronizeValueKafkaComponent implements ApplicationRunner {

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
                Future<RecordMetadata> metadata = producer.send(new ProducerRecord<String, String>("test", "후루루 " + number));
                var value = metadata.get();
                log.info("Partition: {}, Offset: {}", value.partition(), value.offset());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        producer.close();
    }
}
