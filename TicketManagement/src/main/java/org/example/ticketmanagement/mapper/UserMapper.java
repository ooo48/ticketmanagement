package org.example.ticketmanagement.mapper;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.*;
import org.example.ticketmanagement.pojo.UserAddresses;
import org.example.ticketmanagement.pojo.Users;

@Mapper
public interface UserMapper {
    //手机号登录
    @Select("select * from users where phone_number=#{account}")
    Users selectPhone(String account);

    //邮箱登录
    @Select("select * from users where email=#{account}")
    Users selectEmail(String account);

    // 插入新用户
    @Insert("INSERT INTO users(username, password, email, phone_number) VALUES(#{username}, #{password}, #{email}, #{phoneNumber})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 自动返回主键ID
    void registerUser(Users user);

    //查看个人信息
    @Select("select * from users where id=#{id}")
    Users getCurrentUser(Integer id);

    //修改个人信息
    void updateUserInfo(Users user);

    //获取收货地址
    @Select("select address from user_addresses where user_id=#{id}")
    String getAddress(Integer id);

    // 修改收货地址
    void updateAddress(UserAddresses userAddresses);

    // 删除收货地址
    @Delete("delete from user_addresses where user_id=#{id}")
    void deleteAddress(Integer id);
}
