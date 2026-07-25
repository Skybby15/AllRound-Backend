package io.github.skybby15.allround.Service;

import io.github.skybby15.allround.DTO.Authentication.AuthenticationResult;
import io.github.skybby15.allround.DTO.Authentication.LoginRequest;
import io.github.skybby15.allround.DTO.Authentication.RefreshTokenResponse;
import io.github.skybby15.allround.DTO.Authentication.SignupRequest;
import io.github.skybby15.allround.DTO.Authentication.SignupResponse;
import io.github.skybby15.allround.Exception.ApiException;
import io.github.skybby15.allround.Exception.DuplicateAccKeyException;
import io.github.skybby15.allround.Exception.InvalidCredentialsException;
import io.github.skybby15.allround.Model.User;
import io.github.skybby15.allround.Repository.UserRepository;
import io.github.skybby15.allround.Util.PasswordHasher;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.util.Optional;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class AuthenticationService {

  @Inject UserRepository userRepo;
  @Inject JWTParser jwtParser;
  @Inject PasswordHasher hasher;

  public AuthenticationResult loginUser(LoginRequest request) throws InvalidCredentialsException {
    User user =
        userRepo.findByEmail(request.email()).orElseThrow(() -> new InvalidCredentialsException());

    boolean valid = hasher.verifyHashedPassword(user.getPasswordHash(), request.password());
    if (!valid) throw new InvalidCredentialsException();

    String refreshToken =
        Jwt.issuer("allround-auth")
            .subject(user.getId().toString())
            .claim("type", "refresh")
            .expiresIn(Duration.ofDays(10))
            .sign();

    String accessToken = createAccessTokenForUser(user);

    return new AuthenticationResult(accessToken, refreshToken);
  }

  @Transactional
  public SignupResponse signupUser(SignupRequest request) throws DuplicateAccKeyException {

    Optional<User> emailUser = userRepo.findByEmail(request.email());
    if (emailUser.isPresent()) throw new DuplicateAccKeyException("email");

    Optional<User> nameUser = userRepo.findByUsername(request.username());
    if (nameUser.isPresent()) throw new DuplicateAccKeyException("username");

    String hashedPassword = hasher.hashPassword(request.password());

    User newUser =
        User.builder()
            .email(request.email())
            .username(request.username())
            .passwordHash(hashedPassword)
            .build();

    userRepo.persist(newUser);

    return SignupResponse.builder().build();
  }

  public RefreshTokenResponse refreshAccessToken(String refreshToken) throws ApiException {
    User user = checkUserRefreshToken(refreshToken);

    String accessToken = this.createAccessTokenForUser(user);

    return new RefreshTokenResponse(accessToken);
  }

  private String createAccessTokenForUser(User user) {
    return Jwt.issuer("allround-auth")
        .subject(user.getId().toString())
        .upn(user.getEmail())
        .claim("username", user.getUsername())
        .claim("type", "access")
        .expiresIn(Duration.ofMinutes(15))
        .sign();
  }

  private User checkUserRefreshToken(String refreshToken) throws ApiException {
    return checkUserTokenForType(refreshToken, "refresh");
  }

  private User checkUserAccessToken(String accessToken) throws ApiException {
    return checkUserTokenForType(accessToken, "access");
  }

  private User checkUserTokenForType(String token, String type) throws ApiException {
    InvalidCredentialsException generalException =
        new InvalidCredentialsException("Invalid refresh token.");
    JsonWebToken jwt;
    try {
      jwt = jwtParser.parse(token);
    } catch (ParseException err) {
      throw generalException;
    }

    String jwtType = jwt.getClaim("type");
    if (!type.equals(jwtType)) throw generalException;

    Long userId = Long.parseLong(jwt.getSubject());
    Optional<User> user = userRepo.findByIdOptional(userId);
    if (user.isEmpty()) throw generalException;

    return user.get();
  }
}
