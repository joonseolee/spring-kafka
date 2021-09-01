package com.joons.example2;

import com.joons.example2.common.Bar1;
import com.joons.example2.common.Foo1;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaTemplate<Object, Object> template;

    @PostMapping("/send/foo/{what}")
    public void sendFoo(@PathVariable String what) {
        this.template.send("foos", new Foo1(what));
    }

    @PostMapping("/send/bar/{what}")
    public void sendBar(@PathVariable String what) {
        this.template.send("bars", new Bar1(what));
    }

    @PostMapping("/send/unknown/{what}")
    public void sendUnknown(@PathVariable String what) {
        this.template.send("bars", what);
    }
}
