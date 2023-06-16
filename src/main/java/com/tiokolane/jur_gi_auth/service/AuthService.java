package com.tiokolane.jur_gi_auth.service;

import java.util.Optional;

import com.tiokolane.jur_gi_auth.model.PasswordResetToken;
import com.tiokolane.jur_gi_auth.model.User;
import com.tiokolane.jur_gi_auth.payload.LoginDto;
import com.tiokolane.jur_gi_auth.payload.SignUpDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(SignUpDto registerDto);

    void createPasswordResetTokenForUser(Optional<User> user, String token);
    
    String validatePasswordResetToken(String token);

    User getUserByPasswordResetToken(String token);

    void changeUserPassword(User user, String password);

    void deletePasswordResetToken(String token);

    
    
}