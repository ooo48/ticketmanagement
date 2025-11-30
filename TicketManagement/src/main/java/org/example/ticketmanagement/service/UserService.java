package org.example.ticketmanagement.service;

import org.example.ticketmanagement.pojo.UserAddresses;
import org.example.ticketmanagement.pojo.Users;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

public interface UserService {

    Users getCurrentUser(Integer userId);
    void updateUserInfo(Users user);
    String getAddress(Integer userId);
    void updateAddress(UserAddresses userAddresses);
    void deleteAddress(Integer userId);
}
