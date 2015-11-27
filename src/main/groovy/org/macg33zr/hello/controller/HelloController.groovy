package org.macg33zr.hello.controller

import groovy.transform.CompileStatic
import org.macg33zr.hello.queue.Sender
import org.macg33zr.hello.service.HelloService
import org.macg33zr.hello.model.Hello
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.filter.CommonsRequestLoggingFilter

import javax.servlet.http.HttpServletResponse
import java.util.concurrent.atomic.AtomicBoolean;

@CompileStatic
@RestController
class HelloController {

    private final HelloService helloService

    @Autowired
    Sender sender

    @Autowired
    HelloController(HelloService helloService) {
        this.helloService = helloService
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter crlf = new CommonsRequestLoggingFilter()
        crlf.setIncludeClientInfo(true)
        crlf.setIncludeQueryString(true)
        crlf.setIncludePayload(true)
        crlf.setMaxPayloadLength(2048)
        return crlf;
    }

    @RequestMapping(value="/hello")
    Hello hello() {

        Hello hello = helloService.doHello()

        // Hokey
        sender.send(hello)

        return hello
    }

    private final AtomicBoolean sayBusy = new AtomicBoolean(false)

    @RequestMapping(value="/say", method=RequestMethod.POST)
    Hello say(@RequestBody Hello hello, HttpServletResponse response) {

        // Trying to Spock test where concurrent REST calls get busy state, so fake it up here
        if(sayBusy.compareAndSet(false, true)) {
            sender.send(hello)
            Hello helloResponse = helloService.doHello()
            sleep(10000)
            sayBusy.set(false)
            return helloResponse
        }
        else {
            response.setStatus(HttpServletResponse.SC_CONFLICT)
            Hello helloResponse = helloService.doHello()
            return helloResponse
        }

    }


}
