package org.macg33zr.hello.controller

import org.macg33zr.hello.queue.Sender
import org.macg33zr.hello.service.HelloService
import org.macg33zr.hello.model.Hello
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloController {

    private final HelloService helloService

    @Autowired
    Sender sender

    @Autowired
    HelloController(HelloService helloService) {
        this.helloService = helloService
    }

    @RequestMapping("/hello")
    Hello hello() {

        Hello hello = helloService.doHello()

        // Hokey
        sender.send(hello)

        return hello
    }
}
