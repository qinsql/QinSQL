/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.lealone.bats.test.service;

//create service if not exists hello_service ( hello(name varchar) varchar) implement by 'org.lealone.bats.test.service.HelloService';
//execute service hello_service hello('zhh');
public class HelloService {
    public String hello(String name) {
        return "hello " + name;
    }
}
