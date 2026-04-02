package com.generic.api.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.generic.api.handler.ApiException;

/**
 * AuthService
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(AuthRequest request) {
        User user = new User(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password()),
                "USER");
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApiException(404, "usuario nao cadastrado"));

        if (!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new ApiException(401, "senha incorreta");

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
