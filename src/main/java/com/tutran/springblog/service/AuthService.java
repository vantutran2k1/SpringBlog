package com.tutran.springblog.service;

import com.tutran.springblog.payload.authentication.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
