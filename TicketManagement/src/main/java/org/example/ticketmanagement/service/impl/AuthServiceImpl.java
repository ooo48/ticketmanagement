package org.example.ticketmanagement.service.impl;

import org.example.ticketmanagement.mapper.UserMapper;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.pojo.Users;
import org.example.ticketmanagement.service.AuthService;
import org.example.ticketmanagement.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result login(String loginType, String account, String password) {
        Users user = null;
        // 密码登录（手机号）
        if ("password".equals(loginType)) {
            user = userMapper.selectPhone(account);
        }
        // 邮箱登录
        else if ("email".equals(loginType)) {
            user = userMapper.selectEmail(account);
        }
        //用户不存在则注册
        if (user == null) {
            user = new Users();
            // 根据登录类型设置账号（邮箱/手机号）
            if ("email".equals(loginType)) {
                user.setEmail(account);
                user.setUsername("默认用户名");
            }
            else if ("password".equals(loginType)) {
                user.setPhoneNumber(account);
                user.setUsername("默认用户名");
            }
            user.setPassword("123456");
            // 插入新用户
            userMapper.registerUser(user);
            if ("email".equals(loginType)) {
                user = userMapper.selectEmail(account);
            }
            else {
                user = userMapper.selectPhone(account);
            }
        }
        // 密码错误
        else if (!password.equals(user.getPassword())) {
            return Result.error("账号或密码错误");
        }

        // 生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("IP", user.getIP());
        String token = JwtUtils.generateJwt(claims);

        return Result.success(token);
    }

    @Override
    public Result logout(String token) {
        return Result.success("退出成功");
    }
}
