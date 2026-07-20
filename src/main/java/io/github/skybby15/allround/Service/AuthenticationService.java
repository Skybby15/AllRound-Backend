package io.github.skybby15.allround.Service;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import io.github.skybby15.allround.DTO.Authentication.LoginRequest;
import io.github.skybby15.allround.DTO.Authentication.LoginResponse;
import io.github.skybby15.allround.DTO.Authentication.SignupRequest;
import io.github.skybby15.allround.DTO.Authentication.SignupResponse;
import io.github.skybby15.allround.Exception.InvalidCredentialsException;
import io.github.skybby15.allround.Model.User;
import io.github.skybby15.allround.Repository.UserRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.Duration;

@ApplicationScoped
public class AuthenticationService {
  private int hashIterations = 2;
  private int hashMemory = 65536;
  private int hashParallelism = 1;

  @Inject UserRepository userRepo;

  public LoginResponse loginUser(LoginRequest request) {
    User user =
        userRepo.findByEmail(request.email()).orElseThrow(() -> new InvalidCredentialsException());

    Argon2 argon = Argon2Factory.create();
    boolean valid = argon.verify(user.getPasswordHash(), request.password().toCharArray());
    if (!valid) throw new InvalidCredentialsException();

    String token =
        Jwt.issuer("allround-auth")
            .subject(user.getId().toString())
            .upn(user.getEmail())
            .claim("username", user.getUsername())
            .expiresIn(Duration.ofHours(2))
            .sign();

    return LoginResponse.builder().authToken(token).expiresIn(3600L).tokenType("Bearer").build();
  }

  public SignupResponse signupUser(SignupRequest request) {

    Argon2 hasher = Argon2Factory.create();
    String hashedPassword =
        hasher.hash(hashIterations, hashMemory, hashParallelism, request.password().toCharArray());

    User newUser =
        User.builder()
            .email(request.email())
            .username(request.username())
            .passwordHash(hashedPassword)
            .build();

    userRepo.persist(newUser);

    return SignupResponse.builder().build();
  }
}
