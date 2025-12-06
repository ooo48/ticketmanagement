package org.example.ticketmanagement.service.impl;

import org.example.ticketmanagement.mapper.UserMapper;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.pojo.Users;
import org.example.ticketmanagement.service.AuthService;
import org.example.ticketmanagement.utils.EmailUtil;
import org.example.ticketmanagement.utils.JwtUtils;
import org.example.ticketmanagement.utils.VerificationCodeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private VerificationCodeManager codeManager;

    @Override
    public Result login(String loginType, String account, String password) {
        Users user = null;
        // 密码登录（手机号）
        if ("phone_password".equals(loginType)) {
            user = userMapper.selectPhone(account);
            if (!password.equals(user.getPassword())) {
                return Result.error("密码错误");
            }
        }
        // 邮箱密码登录
        else if ("email_password".equals(loginType)) {
            user = userMapper.selectEmail(account);
            if (!password.equals(user.getPassword())) {
                return Result.error("密码错误");
            }
        }
        // 邮箱验证码登录
        else if ("email_code".equals(loginType)) {
            // 验证码验证
            if (!codeManager.verifyCode(account, password)) {
                return Result.error("验证码错误或已过期");
            }
            user = userMapper.selectEmail(account);
        }
        else {
            return Result.error("不支持的登录类型");
        }
        //用户不存在则注册
        if (user == null) {
            user = new Users();
            // 根据登录类型设置账号（邮箱/手机号）
            if ("email_password".equals(loginType)) {
                user.setEmail(account);
                user.setUsername("默认用户名");
            }
            else if ("phone_password".equals(loginType)) {
                user.setPhoneNumber(account);
                user.setUsername("默认用户名");
            }
            user.setPassword("123456");
            // 插入新用户
            userMapper.registerUser(user);
            if ("email".equals(loginType)) {
                user = userMapper.selectEmail(account);
            }
            else if ("phone_password".equals(loginType)){
                user = userMapper.selectPhone(account);
            }
        }

        // 生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("IP", user.getIP());
        claims.put("role", user.getRole());
        String token = JwtUtils.generateJwt(claims);

        return Result.success(token);
    }

    // 发送验证码
    public Result sendVerificationCode(String email) {
        // 检查邮箱格式
        if (!email.contains("@")) {
            return Result.error("邮箱格式不正确");
        }
        // 生成6位验证码
        String code = String.format("%06d", new Random().nextInt(999999));
        // 保存验证码
        codeManager.saveCode(email, code);
        // 发送邮件
        try {
            emailUtil.sendVerificationCode(email, code);
            return Result.success("验证码发送成功");
        } catch (Exception e) {
            return Result.error("验证码发送失败: " + e.getMessage());
        }
    }

    @Override
    public Result logout(String token) {
        return Result.success("退出成功");
    }
}
