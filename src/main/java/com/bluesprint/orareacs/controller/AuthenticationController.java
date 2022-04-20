package com.bluesprint.orareacs.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bluesprint.orareacs.dto.RegisterResponse;
import com.bluesprint.orareacs.entity.User;
import com.bluesprint.orareacs.exception.EmailAlreadyExistsException;
import com.bluesprint.orareacs.exception.MissingRefreshTokenException;
import com.bluesprint.orareacs.exception.TokenValidationException;
import com.bluesprint.orareacs.exception.UsernameAlreadyExistsException;
import com.bluesprint.orareacs.service.TotpManager;
import com.bluesprint.orareacs.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.bluesprint.orareacs.filter.FilterUtils.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
@AllArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private TotpManager totpManager;

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        userService.addUser(user);
        System.out.println(user);

        RegisterResponse response = RegisterResponse.builder()
                .status(HttpStatus.OK.value())
                .secretImageUri(totpManager.getUriForImage(user.getTwoFactorSecret()))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_HEADER)) {
            try {
                String refreshToken = authorizationHeader.substring(TOKEN_HEADER.length());
                Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                Optional<User> userOptional = userService.findUserByUsername(username);
                User user = userOptional.get();

                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TIME_MILLIS))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim(ROLES, new ArrayList<>(List.of(user.getRole())))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put(ACCESS_TOKEN, accessToken);
                tokens.put(REFRESH_TOKEN, refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception exception) {
                throw new TokenValidationException(exception.getMessage());
            }
        } else {
            throw new MissingRefreshTokenException("Refresh token is missing!");
        }
    }
}
