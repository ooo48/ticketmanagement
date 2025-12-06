package org.example.ticketmanagement.controller;

import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.pojo.Shows;
import org.example.ticketmanagement.pojo.Users;
import org.example.ticketmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController {
    @Autowired
    private UserService userService;

    // 获取用户信息
    @GetMapping("/{id}")
    public Result getCurrentUser(@PathVariable Integer id) {
        Users user=userService.getCurrentUser(id);
        return Result.success(user);
    }

    // 修改用户信息
    @PutMapping("/info/{id}")
    public Result updateUserInfo(@PathVariable Integer id,@RequestBody Users user) {
        user.setId(id);
        userService.updateUserInfo(user);
        return Result.success();
    }

    //分页查询用户信息
    @GetMapping
    public Result getUser(@RequestParam String IP,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int size) {
        List<Users> users=userService.getUsers(IP, page, size);
        return Result.success(users);
    }
}
