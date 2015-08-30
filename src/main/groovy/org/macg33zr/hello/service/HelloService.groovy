package org.macg33zr.hello.service

import groovy.transform.CompileStatic
import org.macg33zr.hello.model.Hello
import org.springframework.stereotype.Service

@CompileStatic
@Service
class HelloService {

    Hello doHello() {
        return new Hello([message:"Service says hello from REST endpoint", dateStr:new Date().getDateTimeString()])
    }
}

