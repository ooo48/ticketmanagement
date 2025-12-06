package org.example.ticketmanagement.service;

import org.example.ticketmanagement.pojo.UserAddresses;
import org.example.ticketmanagement.pojo.Users;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.util.List;

public interface UserService {

    Users getCurrentUser(Integer userId);
    void updateUserInfo(Users user);
    String getAddress(Integer userId);
    void updateAddress(UserAddresses userAddresses);
    void deleteAddress(Integer userId);
    List<Users> getUsers(String IP,int page,int size);
}
