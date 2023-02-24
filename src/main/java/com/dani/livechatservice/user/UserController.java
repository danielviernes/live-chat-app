package com.dani.livechatservice.user;

import com.dani.livechatservice.exception.ErrorsEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @GetMapping
    public ResponseEntity<User> getUserByUsername(@RequestParam("username") String username) {
        var user = userService.getUserByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException(ErrorsEnum.USER_NOT_FOUND.getErrorMessage());

        return ResponseEntity.ok(user);
    }

}
