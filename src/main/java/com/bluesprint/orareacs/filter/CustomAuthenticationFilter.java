package com.bluesprint.orareacs.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bluesprint.orareacs.dto.ErrorResponse;
import com.bluesprint.orareacs.dto.LoginCredentials;
import com.bluesprint.orareacs.exception.InvalidCodeException;
import com.bluesprint.orareacs.service.TotpManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.bluesprint.orareacs.filter.FilterUtils.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginCredentials loginCredentials = new LoginCredentials();
        TotpManager totpManager = new TotpManager();
        ConnectionString connectionString = new ConnectionString(DB_CONNECT_URL);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase(DATABASE);
        MongoCollection<Document> collection = database.getCollection(USER_COLLECTION);

        try {
            loginCredentials = objectMapper.readValue(request.getReader(), LoginCredentials.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document query = new Document(USERNAME_FIELD, loginCredentials.getUsername());
        Document result = collection.find(query).first();
        if (result == null || result.getString(TWO_FACTOR_SECRET_FIELD) == null || loginCredentials.getCode() == null ||
                !totpManager.verifyCode(loginCredentials.getCode(), result.getString(TWO_FACTOR_SECRET_FIELD))) {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            ErrorResponse error = ErrorResponse.builder()
                    .message("Invalid code entered!")
                    .status(HttpStatus.FORBIDDEN.value())
                    .timeStamp(System.currentTimeMillis() / 1000)
                    .build();
            try {
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw new InvalidCodeException("Invalid code entered!");
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginCredentials.getUsername(), loginCredentials.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException {
        User user = (User) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TIME_MILLIS))
                .withIssuer(request.getRequestURL().toString())
                .withClaim(ROLES, user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TIME_MILLIS))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put(ROLE, user.getAuthorities().stream().findFirst().get().toString());
        tokens.put(ACCESS_TOKEN, accessToken);
        tokens.put(REFRESH_TOKEN, refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
