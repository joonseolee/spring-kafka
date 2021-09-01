package com.joons.example2;

import com.joons.example2.common.Bar2;
import com.joons.example2.common.Foo2;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.converter.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class Example2Application {

    public static void main(String[] args) {
        SpringApplication.run(Example2Application.class, args);
    }

    @Bean
    public SeekToCurrentErrorHandler errorHandler(KafkaOperations<Object, Object> template) {
        return new SeekToCurrentErrorHandler(
                new DeadLetterPublishingRecoverer(template), new FixedBackOff(1000L, 2));
    }

    @Bean
    public RecordMessageConverter converter() {
        var converter = new ByteArrayJsonMessageConverter();
        var typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        typeMapper.addTrustedPackages("com.joons.common");
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("foo", Foo2.class);
        mappings.put("bar", Bar2.class);
        typeMapper.setIdClassMapping(mappings);
        converter.setTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public NewTopic foos() {
        return new NewTopic("foos", 1, (short) 1);
    }

    @Bean
    public NewTopic bars() {
        return new NewTopic("bars", 1, (short) 1);
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            log.info("Hit Enter to terminate in main class");
            System.in.read();
        };
    }
}
