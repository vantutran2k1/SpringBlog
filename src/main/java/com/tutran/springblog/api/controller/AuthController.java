package com.tutran.springblog.api.controller;

import com.tutran.springblog.api.payload.ApiResponse;
import com.tutran.springblog.api.payload.authentication.JwtAuthResponse;
import com.tutran.springblog.api.payload.authentication.LoginDto;
import com.tutran.springblog.api.payload.authentication.RegisterDto;
import com.tutran.springblog.api.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "REST APIs for Authentication Resource")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> login(@RequestBody @Valid LoginDto loginDto) {
        var response = new JwtAuthResponse();
        response.setAccessToken(authService.login(loginDto));

        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody @Valid RegisterDto registerDto) {
        var response = authService.register(registerDto);
        return new ResponseEntity<>(new ApiResponse<>(response), HttpStatus.CREATED);
    }
}
