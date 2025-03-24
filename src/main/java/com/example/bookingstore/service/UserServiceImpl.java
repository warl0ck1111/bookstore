package com.example.bookingstore.service;

import com.example.bookingstore.config.JwtService;
import com.example.bookingstore.dto.AuthenticationResponse;
import com.example.bookingstore.dto.CreateUserRequest;
import com.example.bookingstore.dto.LoginRequest;
import com.example.bookingstore.entity.User;
import com.example.bookingstore.exceptions.AppUserServiceException;
import com.example.bookingstore.exceptions.UserServiceException;
import com.example.bookingstore.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername/username:{}", username);
        User appUser = userRepository.loadUserByUsername(username.toLowerCase().trim()).orElseThrow(() -> {
            log.error("no user with username: {} found", username);
            return new UserServiceException(String.format("no user with username: %s found", username));
        });
        log.info("loadUserByUsername/currentUserId:{}", appUser.getId());
        return appUser;
    }

    @Override
    public AuthenticationResponse registerUser(CreateUserRequest userRequest) {
        log.info("registerUser/userRequest:{}", userRequest);
        boolean userExists = userRepository.existsByUsernameIgnoreCase(userRequest.getUsername());

        if (userExists) {
            log.error("registerAppUser/user with username {} already exists", userRequest.getUsername());
            throw new AppUserServiceException("username already taken");
        } else if (!StringUtils.equals(userRequest.getPassword(), (userRequest.getConfirmPassword()))) {
            log.error("registerAppUser/password and confirm password do not match");
            throw new AppUserServiceException("password and confirm password do not match");
        }
        log.info("registerUser/registering user:{}", userRequest.getUsername());
        String encodedPassword = passwordEncoder.encode((userRequest.getPassword()));
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(encodedPassword);

        User registeredUser = userRepository.save(user);
        log.info("registerAppUser/ user: {} created successfully", userRequest.getUsername());


        var jwtToken = jwtService.generateToken(registeredUser);
        var refreshToken = jwtService.generateRefreshToken(registeredUser);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userId(registeredUser.getId())
                .username(registeredUser.getUsername()).build();
    }

    @Override
    public AuthenticationResponse loginUser(LoginRequest request) {
        final String username = request.username().trim().toLowerCase();
        log.info("loginAppUser/username:{}", username);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, request.password()));


        User user = (User)loadUserByUsername(request.username());
        if (user != null) {

            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .userId(user.getId())
                    .username(user.getUsername()).build();
        } else log.info("loginUser/user:{} not found", username);
        return null;
    }
}
