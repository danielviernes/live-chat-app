package com.dani.livechatservice.user;

import com.dani.livechatservice.exception.ErrorsEnum;
import com.dani.livechatservice.exception.LiveChatException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @GetMapping
    public ResponseEntity<User> getUserByUsername(@RequestParam("username") String username) {
        var user = userService.loadUserByUsername(username);
        if(user == null)
            throw new LiveChatException(ErrorsEnum.USER_NOT_FOUND);

        return ResponseEntity.ok((User) user);
    }

}
