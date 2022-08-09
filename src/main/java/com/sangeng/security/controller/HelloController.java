package com.sangeng.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther:yukemeng Date:2022/4/12-04-12-19:38
 * Description:
 * version:1.0
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    @PreAuthorize("hasAnyAuthority('system:test:leader')")
    public String hello(){
        return "hello";
    }
}
