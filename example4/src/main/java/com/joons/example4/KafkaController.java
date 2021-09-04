package com.joons.example4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    @Autowired
    private KafkaTemplate<Object, Object> template;

    @PostMapping("/send/{what}")
    public void sendFoo(@PathVariable String what) {
        this.template.send("topic4", what);
    }
}
