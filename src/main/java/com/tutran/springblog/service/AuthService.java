package com.tutran.springblog.service;

import com.tutran.springblog.payload.authentication.LoginDto;
import com.tutran.springblog.payload.authentication.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
