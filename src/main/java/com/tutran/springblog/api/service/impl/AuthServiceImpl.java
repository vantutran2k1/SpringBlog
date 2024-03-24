package com.tutran.springblog.api.service.impl;

import com.tutran.springblog.api.entity.Role;
import com.tutran.springblog.api.exception.UserExistedException;
import com.tutran.springblog.api.mapper.UserMapper;
import com.tutran.springblog.api.payload.authentication.LoginDto;
import com.tutran.springblog.api.payload.authentication.RegisterDto;
import com.tutran.springblog.api.repository.RoleRepository;
import com.tutran.springblog.api.repository.UserRepository;
import com.tutran.springblog.api.security.JwtTokenProvider;
import com.tutran.springblog.api.service.AuthService;
import com.tutran.springblog.api.utils.ErrorMessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Override
    public String login(LoginDto loginDto) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String register(RegisterDto registerDto) {
        var username = registerDto.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new UserExistedException(ErrorMessageBuilder.getUsernameExistedErrorMessage(username));
        }

        var email = registerDto.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new UserExistedException(ErrorMessageBuilder.getEmailExistedErrorMessage(email));
        }

        var user = userMapper.registerDtoToUser(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role userRole = null;
        var userRoleOpt = roleRepository.findByName("USER_ROLE");
        if (userRoleOpt.isPresent()) {
            userRole = userRoleOpt.get();
        }
        user.addRole(userRole);

        userRepository.save(user);

        return "User registered successfully";
    }
}
