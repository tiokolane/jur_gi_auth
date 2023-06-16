package com.tiokolane.jur_gi_auth.service.impl;

import com.tiokolane.jur_gi_auth.model.PasswordResetToken;
import com.tiokolane.jur_gi_auth.model.Role;
import com.tiokolane.jur_gi_auth.model.User;
import com.tiokolane.jur_gi_auth.exception.JurGiAPIException;
import com.tiokolane.jur_gi_auth.payload.LoginDto;
import com.tiokolane.jur_gi_auth.payload.SignUpDto;
import com.tiokolane.jur_gi_auth.repository.PasswordRepository;
import com.tiokolane.jur_gi_auth.repository.RoleRepository;
import com.tiokolane.jur_gi_auth.repository.UserRepository;
import com.tiokolane.jur_gi_auth.security.JwtTokenProvider;
import com.tiokolane.jur_gi_auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private PasswordRepository passwordTokenRepository;


    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           PasswordRepository passwordTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordTokenRepository = passwordTokenRepository;
    }

    @Override
    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    @Override
    public String register(SignUpDto SignupDto) {

        // add check for username exists in database
        if(userRepository.existsByUsername(SignupDto.getUsername())){
            throw new JurGiAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
        }

        // add check for email exists in database
        if(userRepository.existsByEmail(SignupDto.getEmail())){
            throw new JurGiAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }

        User user = new User();
        user.setName(SignupDto.getName());
        user.setUsername(SignupDto.getUsername());
        user.setEmail(SignupDto.getEmail());
        user.setPassword(passwordEncoder.encode(SignupDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return "User registered successfully!.";
    }
    
    @Override
    public void createPasswordResetTokenForUser(Optional<User> user, String token) {
    PasswordResetToken myToken = new PasswordResetToken();
    myToken.setToken(token);
    myToken.setUser(user.get());
    passwordTokenRepository.save(myToken);
    }

    @Override
    public User getUserByPasswordResetToken(String token){
        User user = passwordTokenRepository.findByToken(token).get().getUser();
        return user;
    }
    @Override
    public void changeUserPassword(User user, String password) {
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);
    }

    @Override
    public String validatePasswordResetToken(String token) {
    final PasswordResetToken passToken = passwordTokenRepository.findByToken(token).get();

    return !isTokenFound(passToken) ? "invalidToken"
            // : isTokenExpired(passToken) ? "expired"
            : null;
    }

    @Override
    public void deletePasswordResetToken(String token) {
    final PasswordResetToken passToken = passwordTokenRepository.findByToken(token).get();
    passwordTokenRepository.delete(passToken);
        
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    
}
