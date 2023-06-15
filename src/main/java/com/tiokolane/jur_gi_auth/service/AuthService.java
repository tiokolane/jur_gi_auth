package com.tiokolane.jur_gi_auth.service;

import com.tiokolane.jur_gi_auth.payload.LoginDto;
import com.tiokolane.jur_gi_auth.payload.SignUpDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(SignUpDto registerDto);
}