package org.macg33zr.hello.controller

import groovy.transform.CompileStatic
import org.macg33zr.hello.queue.Sender
import org.macg33zr.hello.service.HelloService
import org.macg33zr.hello.model.Hello
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.filter.CommonsRequestLoggingFilter

import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse
import java.util.concurrent.atomic.AtomicBoolean;

@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@ConditionalOnProperty(name="loadController", havingValue = "true")
@CompileStatic
@RestController
class HelloController {

    /**
     * Hello service providing the back-end service
     */
    private final HelloService helloService

    /**
     * A logger for the controller
     */
    private final Logger log = LoggerFactory.getLogger(this.getClass())

    /**
     * Sender for queue
     */
    @Autowired
    Sender sender

    /**
     * Constructor injecting dependencies
     *
     * @param helloService
     */
    @Autowired
    HelloController(HelloService helloService) {
        this.helloService = helloService
    }

    /**
     * Logging the rest calls
     *
     * @return
     */
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter crlf = new CommonsRequestLoggingFilter()
        crlf.setIncludeClientInfo(true)
        crlf.setIncludeQueryString(true)
        crlf.setIncludePayload(true)
        crlf.setMaxPayloadLength(2048)
        return crlf;
    }

    /**
     * Hello endpoint
     *
     * @return
     */
    @RequestMapping(value="/hello")
    Hello hello() {

        Hello hello = helloService.doHello()

        // Hokey
        sender.send(hello)

        return hello
    }

    /**
     * Retry endpoint
     *
     * @param tries
     * @return
     */
    @RequestMapping(value="/retry/{tries}", method = RequestMethod.GET)
    Hello retry(@PathVariable Integer tries, ServletResponse response) {

        log.info("Retry called with tries: ${tries}")

        Hello hello = helloService.doHelloRetry(tries)

        return hello
    }

    /**
     * Retry endpoint
     *
     * @param tries
     * @return
     */
    @RequestMapping(value="/retrywith/{tries}", method = RequestMethod.GET)
    Hello retryWith(@PathVariable Integer tries, ServletResponse response) {

        log.info("Retry called with tries: ${tries}")

        Hello hello = helloService.doHelloWithTemplate(tries)

        return hello
    }

    /**
     * Say endpoint
     */
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
