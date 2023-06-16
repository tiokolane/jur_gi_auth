package com.tiokolane.jur_gi_auth.controller;

import com.tiokolane.jur_gi_auth.exception.ResourceNotFoundException;
import com.tiokolane.jur_gi_auth.model.User;
import com.tiokolane.jur_gi_auth.payload.JWTAuthResponse;
import com.tiokolane.jur_gi_auth.payload.LoginDto;
import com.tiokolane.jur_gi_auth.payload.PasswordDto;
import com.tiokolane.jur_gi_auth.payload.PasswordRequestChangeDto;
import com.tiokolane.jur_gi_auth.payload.SignUpDto;
import com.tiokolane.jur_gi_auth.repository.UserRepository;
import com.tiokolane.jur_gi_auth.service.AuthService;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    @Autowired
    private UserRepository userRepository;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Build Login REST API
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    // Build Register REST API
    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@RequestBody SignUpDto registerDto){
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordRequestChangeDto passwordDto) {
        Optional<User> user = userRepository.findByEmail(passwordDto.getEmail());
        if (user == null) {
            throw new ResourceNotFoundException();
        }
        String token = UUID.randomUUID().toString();
        authService.createPasswordResetTokenForUser(user, token);
        // mailSender.send(constructResetTokenEmail(getAppUrl(request), 
        // request.getLocale(), token, user));
        // return new GenericResponse(
        // messages.getMessage("message.resetPasswordEmail", null, 
        // request.getLocale()));`
        return new ResponseEntity<>(token, HttpStatus.CREATED);

    }
    @PostMapping("/changePassword")
    public ResponseEntity<String> savePassword(@RequestBody PasswordDto passwordDto) {

        // String result = authService.validatePasswordResetToken(passwordDto.getToken());
        // if(result != null) {
        //     return new GenericResponse(messages.getMessage(
        //         "auth.message." + result, null, locale));
        // }

        User user = authService.getUserByPasswordResetToken(passwordDto.getToken());
        if(user != null) {
            authService.changeUserPassword(user, passwordDto.getNewPassword());
            String message = "Mot de passe changé avec succes";
            authService.deletePasswordResetToken(passwordDto.getToken());
            return new  ResponseEntity<>(message, HttpStatus.OK);
        } else {
           throw new ResourceNotFoundException();
        }
    }
}