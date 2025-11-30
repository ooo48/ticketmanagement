package org.example.ticketmanagement.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.pojo.UserAddresses;
import org.example.ticketmanagement.pojo.Users;
import org.example.ticketmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // 获取当前用户信息
    @GetMapping
    public Result getCurrentUser(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        Users user=userService.getCurrentUser(userId);
        return Result.success(user);
    }

    // 修改个人信息
    @PutMapping("/info")
    public Result updateUserInfo(HttpServletRequest request,@RequestBody Users user) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        user.setId(userId);
        userService.updateUserInfo(user);
        return Result.success();
    }

    // 获取收货地址
    @GetMapping("/address")
    public Result getAddress(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        String address=userService.getAddress(userId);
        return Result.success(address);
    }

    // 修改收货地址
    @PutMapping("/address")
    public Result updateAddress(HttpServletRequest request,@RequestBody UserAddresses userAddresses) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        userAddresses.setUserId(userId); // 设置当前登录用户ID
        userService.updateAddress(userAddresses);
        return Result.success();
    }

    // 删除收货地址
    @DeleteMapping("/address")
    public Result deleteAddress(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        userService.deleteAddress(userId);
        return Result.success();
    }
}
