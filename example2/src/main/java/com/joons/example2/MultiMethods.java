package com.joons.example2;

import com.joons.example2.common.Bar2;
import com.joons.example2.common.Foo2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@KafkaListener(id = "multiGroup", topics = { "foos", "bars" })
public class MultiMethods {

    private final TaskExecutor exec = new SimpleAsyncTaskExecutor();

    @KafkaHandler
    public void foo(Foo2 foo) {
        log.info("Received foo: " + foo);
        terminateMessage();
    }

    @KafkaHandler
    public void bar(Bar2 bar) {
        log.info("Received bar: " + bar);
        terminateMessage();
    }

    @KafkaHandler
    public void unknown(Object obj) {
        log.info("Received unknown: " + obj);
        terminateMessage();
    }

    private void terminateMessage() {
        this.exec.execute(() -> log.info("Hit Enter to terminate..."));
    }
}
