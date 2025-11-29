package org.example.ticketmanagement.mapper;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.ticketmanagement.pojo.Users;

@Mapper
public interface UserMapper {
    //查看个人信息
    @Select("select * from users where id=#{id}")
    Users getCurrentUser(Integer id);

    //修改个人信息
    void updateUserInfo(Users user);

    //获取收货地址
    @Select("select address from user_addresses where user_id=#{id}")
    String getAddress(Integer id);

    // 修改收货地址
    @Update("update user_addresses set address=#{address}")
    void updateAddress(String address);

    // 删除收货地址
    @Delete("delete from user_addresses where user_id=#{id}")
    void deleteAddress(Integer id);
}
