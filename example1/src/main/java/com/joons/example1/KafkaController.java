package com.joons.example1;

import com.joons.example1.common.Foo1;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaTemplate<Object, Object> templates;

    @PostMapping("/send/foo/{what}")
    public void sendFoo(@PathVariable String what) {
        this.templates.send("topic1", new Foo1(what));
    }
}
