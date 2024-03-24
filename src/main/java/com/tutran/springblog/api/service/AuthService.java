package com.tutran.springblog.api.service;

import com.tutran.springblog.api.payload.authentication.LoginDto;
import com.tutran.springblog.api.payload.authentication.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
