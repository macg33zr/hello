package org.macg33zr.hello.service

import groovy.transform.CompileStatic
import org.macg33zr.hello.model.Hello
import org.springframework.stereotype.Service

@CompileStatic
@Service
class HelloService {

    Hello doHello() {
        return new Hello([message:"Service says hello", dateStr:new Date().getDateTimeString()])
    }
}

