package org.macg33zr.hello.model

import groovy.transform.ToString

@ToString(includeFields = true,includeNames = true)
class Hello implements Serializable{
    String message
    String dateStr
    String uuid = UUID.randomUUID().toString()
}
