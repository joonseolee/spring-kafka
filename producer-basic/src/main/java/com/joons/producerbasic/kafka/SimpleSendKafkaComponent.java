package com.joons.producerbasic.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Properties;
import java.util.stream.IntStream;

/**
 * 메시지를 보내고 확인하지않는 경우
 * 실제에서 이렇게 사용은 안하지만 테스트겸
 */
//@Service
public class SimpleSendKafkaComponent implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092");
        props.put("acks", "1"); // 프로듀서가 카프카 토픽의 리더에게 메시지를 보낸후 요청을 완료하기 전 ack(승인)의 수  높으면 성능좋지만 메시지 손실가능성 높아짐, 반대로는 성는ㅇ에 좋지않지만 메시지 손실가능성 낮아짐
        props.put("compression.type", "gzip"); // 프로듀서가 데이터를 압축해서 보낼수있는데 타입을 설정
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);

        IntStream.rangeClosed(1, 10).forEach(number -> {
            producer.send(new ProducerRecord<String, String>("test", "후후후후후 " + number));
        });
        producer.close();
    }
}
