package org.example.ticketmanagement.service.impl;

import org.example.ticketmanagement.mapper.UserMapper;
import org.example.ticketmanagement.pojo.UserAddresses;
import org.example.ticketmanagement.pojo.Users;
import org.example.ticketmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Users getCurrentUser(Integer userId) {
        return userMapper.getCurrentUser(userId);
    }

    @Override
    public void updateUserInfo(Users user) {
        userMapper.updateUserInfo(user);
    }

    @Override
    public String getAddress(Integer userId) {
        return userMapper.getAddress(userId);
    }

    @Override
    public void updateAddress(UserAddresses userAddresses) {
        userMapper.updateAddress(userAddresses);
    }

    @Override
    public void deleteAddress(Integer userId) {
        userMapper.deleteAddress(userId);
    }
    @Override
    public List<Users> getUsers(String IP, int page, int size){
        int index=(page-1)*size;
        return userMapper.getUsers(IP, index, size);
    }
}
