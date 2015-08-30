package org.macg33zr.hello.queue

import org.macg33zr.hello.model.Hello
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class Consumer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public void process(Hello item) {

        log.info("Recieved: ${item.toString()}")

    }
}
