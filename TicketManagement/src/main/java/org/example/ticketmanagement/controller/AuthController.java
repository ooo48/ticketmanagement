package org.example.ticketmanagement.controller;

import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    // 登录接口（支持多类型登录）
    @PostMapping("/login")
    public Result login(@RequestParam String loginType, @RequestParam String account, @RequestParam String password) {
        return authService.login(loginType, account, password);
    }

    // 退出登录
    @PostMapping("/logout")
    public Result logout(@RequestHeader String token) {
        return authService.logout(token);
    }

}
