package com.startit.authservice.service;

import com.startit.authservice.exception.BadRegistrationDataException;
import com.startit.authservice.exception.UserExistsException;
import com.startit.authservice.jwt.JwtService;
import com.startit.authservice.transfer.AuthResponse;
import com.startit.authservice.transfer.Credentials;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final UserServiceClient userServiceClient;

    public AuthResponse register(Credentials request) {
        var reqUser = userServiceClient.findByUsername(request.getUser().getUsername());

        if (reqUser.isPresent()) {
            throw new UserExistsException("Пользователь с таким именем уже существует");
        } else if (request.getUser().getUsername().length() < 3) {
            throw new BadRegistrationDataException("Имя пользователя должно быть не менее 3-х символов");
        } else if (request.getPassword().length() < 3) {
            throw new BadRegistrationDataException("Пароль должен быть не меньше 3-х символов");
        }

        var user = request.getUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userServiceClient.save(user);

        var jwtToken = jwtService.generateToken(user.getUsername());
        var refreshToken = jwtService.generateRefreshToken(user.getUsername());

        return AuthResponse.builder()
                .isValid(true)
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse login(Credentials request) {
        var reqUser = userServiceClient.findByUsername(request.getUser().getUsername());
        if (reqUser.isEmpty())
            throw new UserExistsException("Пользователя под таким именем не существует");

        var rawUser = reqUser.get();
        if (!passwordEncoder.matches(request.getPassword(), rawUser.getPassword()))
            throw new BadRegistrationDataException("Неверный пароль пользователя");

        var jwtToken = jwtService.generateToken(rawUser.getUsername());
        var refreshToken = jwtService.generateRefreshToken(rawUser.getUsername());

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}
