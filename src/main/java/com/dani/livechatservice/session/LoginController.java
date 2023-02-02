package com.dani.livechatservice.session;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return "Hello World!";
    }

}
