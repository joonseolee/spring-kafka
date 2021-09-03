package com.joons.example3;

import com.joons.example3.common.Foo1;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    @Autowired
    private KafkaTemplate<Object, Object> template;

    @PostMapping("/send/foos/{what}")
    public void sendFoo(@PathVariable String what) {
        this.template.executeInTransaction(kafkaTemplate -> {
            StringUtils.commaDelimitedListToSet(what).stream()
                    .map(Foo1::new)
                    .forEach(foo -> kafkaTemplate.send("topic2", foo));
            return null;
        });
    }
}
