package org.example.ticketmanagement.service;

import org.example.ticketmanagement.pojo.Result;

public interface AuthService {

    Result login(String loginType, String account, String password);
    Result logout(String token);

}
